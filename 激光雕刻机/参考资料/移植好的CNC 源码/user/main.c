/******************** (C) COPYRIGHT 2013 STMicroelectronics ********************
* File Name          : main.c
* Author             : 
* Version            : V2.0.1
* Date               : 01/09/2014
* Description        : 
*******************************************************************************/
#include "Config.h"
#include <string.h>
#include <stdlib.h>
#include <stdio.h>


#ifdef __GNUC__
  /* With GCC/RAISONANCE, small printf (option LD Linker->Libraries->Small printf
     set to 'Yes') calls __io_putchar() */
  #define PUTCHAR_PROTOTYPE int __io_putchar(int ch)
#else
  #define PUTCHAR_PROTOTYPE int fputc(int ch, FILE *f)
#endif /* __GNUC__ */

char buffer1[4096];   	/* file copy buffer */
char writefbuff[1024];

uint16 pc_port_commun=8000;		/*定义一个任意端口并初始化	通信使用*/	
#define  SOCK_COMMUN	 1	
	
unsigned int Host_order;
uint8 buffer[2048];		/*定义一个2KB的缓存*///网络缓存
	
void SocketHandle(void);	
void WorkingFromSDfile(void);	
void TimerSetup4(void);
	
FATFS fatfs;
FRESULT fres;
	
int main(void)
{
	static unsigned char run_cycle_count = 0;
//	int i,val = 1;
    BSP_Init();										//Board Init
	OutputControl(0x0000);				//关闭所有端口
	
	settings_init();
//	settings_reset(true);				//使用默认参数
	st_init(); 
	
	memset(&sys, 0, sizeof(sys));
	sys.abort = true;   // Set abort to complete initialization
  sys.state = STATE_INIT;  // Set alarm state to indicate unknown initial position 
	init_grbl();
//	TimerSetup4();
	PWR_ON; 
	Delay_MS(100);
	if (RCC_GetFlagStatus(RCC_FLAG_HSERDY) == SUCCESS)
	{
	//	DebugPf("*** CNC Control system boot ok***\r\n");
	}
	else
	{
		//DebugPf("*** Boot Fail [ HSE ]");
	}
	
	fres = f_mount(0, &fatfs);	//挂载SD卡
	if (fres != FR_OK)
	{
		//DebugPf("Mount SD error [0x%x]\n", fres);
	}
	else
	{
		DWORD fre_clust;
		FATFS *fs1;
		fres = f_getfree("", &fre_clust, &fs1);
	//	DebugPf("FreeSpace:%ld MB\n",(fre_clust /2048));
	}
	
//	for(i=0;i<16;i++)
//	{		
//		OutputControl(val);
//		val<<=1;
//		Delay_MS(100);
//	}
	beep(500);	
	FAN_ON;
	
	if(ReadTestboardID() == 1)
	{
		WorkingFromSDfile();	//读取SD卡文件并执行
	}

	while(1)
	{
		protocol_process();
//		SocketHandle();	
	
		/********************************/ //MCU运行指示灯
		Delay_MS(1);	
		run_cycle_count++;
				
		if(run_cycle_count == 100)
		{
			LED_LED1_ON();
		}	
		else if(run_cycle_count >= 200)
		{
			LED_LED1_OFF();
			run_cycle_count = 0;
		}
		/********************************/
	} 
}

///*******************************************************************************
//* Function Name  : 串口处理
//* Description    : 
//********************************************************************************/
//void SocketHandle(void)
//{
//	static uint8 status = 0;
//	uint16 len=0;

//		status = getSn_SR(SOCK_COMMUN);		//8000端口
//    switch(status)			/*获取socket0的状态*/
//    {
//       case SOCK_INIT: 	/*socket初始化完成*/
//			 
//         //connect(0, pc_ip ,pc_port);							/*在TCP Client模式下向服务器发送连接请求*/ 
//				 listen(SOCK_COMMUN);												/*在TCP Server模式下向服务器发送连接请求*/
//				 DebugPf("1 W5500 connecting \r\n");
//         break;
//			 
//       case SOCK_ESTABLISHED:									/*socket连接建立*/ 
//				 
//         if(getSn_IR(SOCK_COMMUN) & Sn_IR_CON)
//         {
//            setSn_IR(SOCK_COMMUN, Sn_IR_CON);	/*Sn_IR的第0位置1*/
//         }      
//         len=getSn_RX_RSR(SOCK_COMMUN);				/*len为已接收数据的大小*/
//         if(len>0)
//         {
//            recv(SOCK_COMMUN,buffer,len);			/*W5500接收来自Sever的数据*/
//         }
//         break;
//       case SOCK_CLOSE_WAIT:									/*socket等待关闭状态*/
//					DebugPf("1 W5500 wait close \r\n");
//					close(SOCK_COMMUN);
//					break;
//       case SOCK_CLOSED:											/*socket关闭*/
//					DebugPf("1 W5500 closed \r\n");
//         	socket(SOCK_COMMUN,Sn_MR_TCP,pc_port_commun,Sn_MR_ND);/*打开socket0的一个端口*/
//					break;
//    }	
//}


void WorkingFromSDfile()
{

uint32_t len = 0;
FRESULT res;
FIL file;
	
		sys.abort = 0;
		sys.auto_start = true;//true
	
		res = f_open(&file,"0:/test/data.nc", FA_OPEN_EXISTING | FA_READ);
		if (res == FR_OK) 
		{
				char riadok[100];

				while (f_gets(riadok, sizeof (riadok), &file) != NULL)  
				{
					len++;
					gc_execute_line(riadok);			//解析文件行不可带\r
					DebugPf("[Gcode %d] %s",len,riadok);
					protocol_execute_runtime();	//	CNC
					
//					while(sys.state == STATE_CYCLE)	//等待单步执行完毕
//					{
//						if(sys.state == STATE_HOLD) break;
//						if(sys.state == STATE_IDLE) break;
//						if(sys.execute == EXEC_CYCLE_STOP) break;
//					}			

					if(sys.abort) break;
				}
				DebugPf("Work finish\r\n");
		}	
		sys.abort = 0;
		f_close (&file); 
}


//void TimerSetup4(void) 
//{
//	  RCC->APB1ENR |= RCC_APB1ENR_TIM4EN;                     // enable clock for TIM4

////		TIM4->PSC = 1124;                          // set prescaler
////		TIM4->PSC = 28;                          // set prescaler	1/40sekundy
//		TIM4->PSC = 10;                          // set prescaler	1/40sekundy
//		TIM4->ARR = 63999;  											// set auto-reload
//		TIM4->CR1 = 0;                            // reset command register 1
//		TIM4->CR2 = 0;                            // reset command register 2
//		TIM4->DIER = (1<<0);                      // Update interrupt enabled
//		TIM4->CR1 |= (1<<0);                      // Enable Timer
//		NVIC_EnableIRQ (TIM4_IRQn);               // Enable TIM4 interrupt
//		NVIC_SetPriority(TIM4_IRQn, 3);
//} 

//void TIM4_IRQHandler(void)
//{
//	static char Timer4Ticks = 0;
//	
//  if ((TIM4->SR & 0x0001) != 0)                  // check interrupt source
//	{
//    TIM4->SR &= ~(1<<0);                          // clear UIF flag
//		if(ShowPos)
//		{
//				lcd_x = 50 + (sys.position[X_AXIS] / (settings.steps_per_mm[X_AXIS] / 6));
//				lcd_y = 50 + (sys.position[Y_AXIS] / (settings.steps_per_mm[Y_AXIS] / 6));

//				Timer4Ticks++;
//				if(Timer4Ticks >= 60)
//				{
//					printpos_grbl(0,480);
//					Timer4Ticks = 0;
//				}
//		}
//	}
//}

