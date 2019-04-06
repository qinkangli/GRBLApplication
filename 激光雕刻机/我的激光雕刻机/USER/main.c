#include "stm32f10x.h"
#include "Lcd_Driver.h"
#include "flash.h"
#include "picture.h"
#include "led.h"
#include "GUI.h"
#include "Key.h"
#include "delay.h"
#include "usart.h"
#include "sys.h"
#include "math.h"
#include "stdio.h"
#include "malloc.h"
#include "usart2.h"

//蓝牙连接标志
#define BT_ON GREEN
#define BT_OFF RED

//Arduino连接标志
#define AVR_ON GREEN
#define AVR_OFF RED



#define SIZE 32776
u8 datatemp[2];//flash读取数据缓存区
u8 LCDtemp[110]={"This project is design by Eagle if you have any question please connect me ^_^ QQ: 1460853569 Tel:13679427579"};//显示屏显示字符缓存区

extern u16 BACK_COLOR;
/*********
G01 X+1   X轴加一
G01 X-1	  X轴减一
G01 Y+1   Y轴加一
G01 Y-1   Y轴键一
M4    激光开(强)
M3    激光开(弱)
M2    激光关
********/


void GUI_Menu(void);
void Cache_Display(u8 *s);



int main(void)
{	
	u32 Loop; 
	u8 X=1,Y=1;
	u8 LasFlag=0;

	u8 WR_COM=0,space_pos;
	u16 i=0,count;
	u32 byte_len =0;
	
	symbol_enable=1; 
	USART_RX_STA=0;
	
	
	delay_init();
	NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2);
	uart_init(9600);
	
	W25QXX_Init();
//	my_mem_init(SRAMEX);		//初始化内部内存池
	
	LED_Init();
	
	KEY_Init();
	
	
	Lcd_Init();
	LCD_LED_SET;
	Lcd_Clear(WHITE);
	
	


	if(W25QXX_ReadID()==0XEF17)//0XEF17,表示芯片型号为W25Q128 
	{
		LED=0;
		delay_ms(200);
		LED=1;
		delay_ms(200);
		LED=0;
		delay_ms(200);
		LED=1;
		//读取成功
//		W25QXX_Write((u8*)gImage_Logo,1,SIZE);
//		printf("****Flah芯片型号为W25Q128****\r\n");
//		printf("****写入的SIZE:%d****\r\n",SIZE);
	}
	Lcd_SetRegion(0,0,127,127);		//坐标设置
	for(Loop=1;Loop<=127*127;Loop++)
	{	
		W25QXX_Read(datatemp,Loop*2+1,2);//读取两字节存放在datatemp数组中
		LCD_WriteData_16Bit(datatemp[1]<<8|datatemp[0]); 			
	}//将flash里的数据读取出来并打印在显示屏上
	delay_ms(1000);//延迟一秒
	printf("M0\r\n");//关闭激光头
	Lcd_Clear(LIGHTBLUE);
	GUI_Menu(); //显示屏初始界面
	while(1)
	{	
//	Gui_DrawFont_GBK24(16,30,YELLOW,GRAY0,"纯色填充");
//	Gui_DrawFont_GBK24(16,50,BLUE,GRAY0,"纯色填充");
//	Gui_DrawFont_GBK24(16,70,RED,GRAY0, "纯色填充");
//	Gui_DrawFont_GBK16(0,100,BLUE,GRAY0,"Tel:18639000975");
//	Gui_DrawFont_GBK16(0,130,RED,GRAY0, "QQ:2534656669");
				
		if(X_KEY==0)
		{
			delay_ms(10);
			if(X_KEY==0)
			{
				while(!X_KEY);
				X=X+1;
				LED=0;
				delay_ms(10);
				LED=1;
				printf("G91\r\n");
				printf("G01 X1\r\n");
			}
		}
		if(Y_KEY==0)
		{
			delay_ms(10);
			if(Y_KEY==0)
			{
				while(!Y_KEY);
				Y=Y+1;
				LED=0;
				delay_ms(10);
				LED=1;
				printf("G91\r\n");
				printf("G01 Y1\r\n");
			}
		}
		if(LAS_KEY==0)
		{
			delay_ms(10);
			if(LAS_KEY==0)
			{
				LasFlag++;
				if(LasFlag==2)  LasFlag=0;
				LED=0;
				delay_ms(10);
				LED=1;
				while(!LAS_KEY);
				if(LasFlag==1)
				printf("M4\r\n");
				if(LasFlag==0)
				printf("M2\r\n");
			}
		}

			

	}
}

void GUI_Menu(void)
{
	u8 i=5;
	BACK_COLOR=LIGHTBLUE;
	Gui_DrawLine(90,  0,  90,96, GRAY2);
	Gui_DrawLine(91,  0,  91,96, GRAY2);
	
	Gui_DrawLine(0,  96,  127,96, GRAY2);
	Gui_DrawLine(0,  97,  127,97, GRAY2);

	LCD_ShowString(96,0,60,12,12,(u8*)"Bound");//12*12
	
	LCD_ShowString(98,16,48,12,12,(u8*)"9600");
	DisplayButtonUp(94,13,97+29,16+14);
	
	LCD_ShowString(98,32,24,12,12,(u8*)" BT ");
	
	for(i=5;i>0;i--)
	Gui_Circle(110,51,i,BT_OFF); 
	DisplayButtonUp(94,44,97+29,44+16);
	
	LCD_ShowString(102,62,60,12,12,(u8*)"AVR");
	
	for(i=5;i>0;i--)
	Gui_Circle(110,83,i,AVR_OFF); 
	DisplayButtonUp(94,75,97+29,75+16);
	

	
//	Cache_Display(LCDtemp);
//	BACK_COLOR=RED;
	LCD_ShowString(0,0,90,12,12, (u8*)"");
	LCD_ShowString(0,12,90,12,12,(u8*)"");
	LCD_ShowString(0,24,90,12,12,(u8*)"");
	LCD_ShowString(0,36,90,12,12,(u8*)"");
	LCD_ShowString(0,48,90,12,12,(u8*)" ");
	LCD_ShowString(0,60,90,12,12,(u8*)" ");
	LCD_ShowString(0,72,90,12,12,(u8*)"");
	LCD_ShowString(0,84,90,12,12,(u8*)"");
}

void Cache_Display(u8 *s)
{
	u8 i=0;
	u8 j=0;
	u8 k=0;
	u8 temp[15];

	for(i=0;i<=110;i++)
	{

		temp[i%15]=s[i];
		
		if(i%15>=14)
		{
//			if(temp[0]==' ')
//			{
//				for(k=0;k<15;k++)
//				temp[k]=temp[k+1];
//					
//			}
			LCD_ShowString(0,12*j,90,12,12,temp);
			j++;	
		}
		
	}

}













