#include "GPIO_Init.h"
#include "platform_config.h"

Type_Input_Port Input_Port ;
Type_Output_Port Output_Port ;
/*******************************************************************************
* Function Name  : GPIO_Configuration
* Description    : Configures the different GPIO ports.
* Input          : None
* Output         : None
* Return         : None
*******************************************************************************/
void GPIO_Config(void)
{
  GPIO_InitTypeDef GPIO_InitStructure;

  RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOA|
												 RCC_APB2Periph_GPIOB|
												 RCC_APB2Periph_GPIOC|
												 RCC_APB2Periph_GPIOD|
												 RCC_APB2Periph_GPIOE|
												 RCC_APB2Periph_GPIOF|
												 RCC_APB2Periph_GPIOG, ENABLE);		//开时钟
	
	//LED														 
  GPIO_InitStructure.GPIO_Pin = GPIO_Pin_1;									
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		//时钟速度为50M
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP;		//端口模式为推拉输出方式	
  GPIO_Init(GPIOA, &GPIO_InitStructure);			
	//Power Control
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_14 |GPIO_Pin_15;									
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		//时钟速度为50M
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP;		//端口模式为推拉输出方式	
  GPIO_Init(GPIOE, &GPIO_InitStructure);	
	
	//bei ma kai guan LY
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_9 |GPIO_Pin_10 |GPIO_Pin_11 ;									
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IPU;				//端口模式为上拉输入方式	
  GPIO_Init(GPIOD, &GPIO_InitStructure); 
	
	//Input
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_0 |GPIO_Pin_1 ;									
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IPU;				//端口模式为上拉输入方式	
  GPIO_Init(GPIOB, &GPIO_InitStructure); 
	
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_0 |GPIO_Pin_1 ;									
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IPU;				//端口模式为上拉输入方式	
  GPIO_Init(GPIOG, &GPIO_InitStructure); 
	
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_0 |GPIO_Pin_1 |
																GPIO_Pin_2 |GPIO_Pin_3 |
																GPIO_Pin_4 |GPIO_Pin_5 |
																GPIO_Pin_6 |GPIO_Pin_7 |
																GPIO_Pin_8 |GPIO_Pin_9 |
																GPIO_Pin_10|GPIO_Pin_11|
																GPIO_Pin_12|GPIO_Pin_13|
																GPIO_Pin_14|GPIO_Pin_15;									
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IPU;				//端口模式为上拉输入方式	
  GPIO_Init(GPIOF, &GPIO_InitStructure);
	
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_2 |GPIO_Pin_3 |
																GPIO_Pin_4 |GPIO_Pin_5 |
																GPIO_Pin_6 |GPIO_Pin_7 |
																GPIO_Pin_8 |GPIO_Pin_9 |
																GPIO_Pin_10|GPIO_Pin_11|
																GPIO_Pin_12|GPIO_Pin_13;									
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IPU;				//端口模式为上拉输入方式	
  GPIO_Init(GPIOE, &GPIO_InitStructure);

	//Output
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_8;									
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		//时钟速度为50M
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP;		//端口模式为推拉输出方式	
  GPIO_Init(GPIOA, &GPIO_InitStructure);	
	
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_7 |GPIO_Pin_14 |GPIO_Pin_15;									
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		//时钟速度为50M
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP;		//端口模式为推拉输出方式	
  GPIO_Init(GPIOD, &GPIO_InitStructure);
	
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_6 |GPIO_Pin_7 |GPIO_Pin_8 |GPIO_Pin_9;								
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		//时钟速度为50M
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP;		//端口模式为推拉输出方式	
  GPIO_Init(GPIOC, &GPIO_InitStructure);
	
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_2 |GPIO_Pin_3 |GPIO_Pin_4 |GPIO_Pin_5 |GPIO_Pin_6 |GPIO_Pin_12 |GPIO_Pin_13 |GPIO_Pin_14;										
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		//时钟速度为50M
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP;		//端口模式为推拉输出方式	
  GPIO_Init(GPIOG, &GPIO_InitStructure);
}



///*******************************************************************************
//* Function Name  :
//* Description    : 控制外部输出
//********************************************************************************/

void OutputControl(unsigned int stats)
{
    Output_Port.ALL=~stats;
	
    if(Output_Port.ONE.Y_1==0)
    { OUT_Y1_ON; }
    else
    { OUT_Y1_OFF;}

    if(Output_Port.ONE.Y_2==0)
    { OUT_Y2_ON; }
    else
    { OUT_Y2_OFF;}

    if(Output_Port.ONE.Y_3==0)
    { OUT_Y3_ON; }
    else
    { OUT_Y3_OFF;}
    if(Output_Port.ONE.Y_4==0)
    { OUT_Y4_ON; }
    else
    { OUT_Y4_OFF;}

    if(Output_Port.ONE.Y_5==0)
    { OUT_Y5_ON; }
    else
    { OUT_Y5_OFF;}

    if(Output_Port.ONE.Y_6==0)
    { OUT_Y6_ON; }
    else
    { OUT_Y6_OFF;}
     if(Output_Port.ONE.Y_7==0)
    { OUT_Y7_ON; }
    else
    { OUT_Y7_OFF;}

    if(Output_Port.ONE.Y_8==0)
    { OUT_Y8_ON; }
    else
    { OUT_Y8_OFF;}

    if(Output_Port.ONE.Y_9==0)
    { OUT_Y9_ON; }
    else
    { OUT_Y9_OFF;}
    if(Output_Port.ONE.Y_10==0)
    { OUT_Y10_ON; }
    else
    { OUT_Y10_OFF;}

    if(Output_Port.ONE.Y_11==0)
    { OUT_Y11_ON; }
    else
    { OUT_Y11_OFF;}

    if(Output_Port.ONE.Y_12==0)
    { OUT_Y12_ON; }
    else
    { OUT_Y12_OFF;}

    if(Output_Port.ONE.Y_13==0)
    { OUT_Y13_ON; }
    else
    { OUT_Y13_OFF;}
    if(Output_Port.ONE.Y_14==0)
    { OUT_Y14_ON; }
    else
    { OUT_Y14_OFF;}
    if(Output_Port.ONE.Y_15==0)
    { OUT_Y15_ON; }
    else
    { OUT_Y15_OFF;}
    if(Output_Port.ONE.Y_16==0)
    { OUT_Y16_ON; }
    else
    { OUT_Y16_OFF;}
}

/*******************************************************************************
* Function Name  :
* Description    : 读取输入状态 0L 1H
********************************************************************************/
unsigned char InputRead(unsigned char ch)
{
    Input_Port.ALL=0;           //清空
		if(ch == 0)
		{
			if(IN_X1==1)
			{ Input_Port.ONE.X_1=1; }
			else
			{ Input_Port.ONE.X_1=0; }

			if(IN_X2==1)
			{ Input_Port.ONE.X_2=1; }
			else
			{ Input_Port.ONE.X_2=0; }

			if(IN_X3==1)
			{ Input_Port.ONE.X_3=1; }
			else
			{ Input_Port.ONE.X_3=0; }

			if(IN_X4==1)
			{ Input_Port.ONE.X_4=1; }
			else
			{ Input_Port.ONE.X_4=0; }
			
			if(IN_X5==1)
			{ Input_Port.ONE.X_5=1; }
			else
			{ Input_Port.ONE.X_5=0; }

			if(IN_X6==1)
			{ Input_Port.ONE.X_6=1; }
			else
			{ Input_Port.ONE.X_6=0; }

			if(IN_X7==1)
			{ Input_Port.ONE.X_7=1; }
			else
			{ Input_Port.ONE.X_7=0; }

			if(IN_X8==1)
			{ Input_Port.ONE.X_8=1; }
			else
			{ Input_Port.ONE.X_8=0; }
		}
		else if(ch == 1)
		{
			if(IN_X9==1)
			{ Input_Port.ONE.X_1=1; }
			else
			{ Input_Port.ONE.X_1=0; }

			if(IN_X10==1)
			{ Input_Port.ONE.X_2=1; }
			else
			{ Input_Port.ONE.X_2=0; }

			if(IN_X11==1)
			{ Input_Port.ONE.X_3=1; }
			else
			{ Input_Port.ONE.X_3=0; }

			if(IN_X12==1)
			{ Input_Port.ONE.X_4=1; }
			else
			{ Input_Port.ONE.X_4=0; }

			if(IN_X13==1)
			{ Input_Port.ONE.X_5=1; }
			else
			{ Input_Port.ONE.X_5=0; }

			if(IN_X14==1)
			{ Input_Port.ONE.X_6=1; }
			else
			{ Input_Port.ONE.X_6=0; }

			if(IN_X15==1)
			{ Input_Port.ONE.X_7=1; }
			else
			{ Input_Port.ONE.X_7=0; }
			
			if(IN_X16==1)
			{ Input_Port.ONE.X_8=1; }
			else
			{ Input_Port.ONE.X_8=0; }
		}
		else if(ch == 2)
		{
			if(IN_X17==1)
			{ Input_Port.ONE.X_1=1; }
			else
			{ Input_Port.ONE.X_1=0; }

			if(IN_X18==1)
			{ Input_Port.ONE.X_2=1; }
			else
			{ Input_Port.ONE.X_2=0; }

			if(IN_X19==1)
			{ Input_Port.ONE.X_3=1; }
			else
			{ Input_Port.ONE.X_3=0; }

			if(IN_X20==1)
			{ Input_Port.ONE.X_4=1; }
			else
			{ Input_Port.ONE.X_4=0; }

			if(IN_X21==1)
			{ Input_Port.ONE.X_5=1; }
			else
			{ Input_Port.ONE.X_5=0; }

			if(IN_X22==1)
			{ Input_Port.ONE.X_6=1; }
			else
			{ Input_Port.ONE.X_6=0; }

			if(IN_X23==1)
			{ Input_Port.ONE.X_7=1; }
			else
			{ Input_Port.ONE.X_7=0; }

			if(IN_X24==1)
			{ Input_Port.ONE.X_8=1; }
			else
			{ Input_Port.ONE.X_8=0; }
		}
		else if (ch ==3)		
		{
			if(IN_X25==1)
			{ Input_Port.ONE.X_1=1; }
			else
			{ Input_Port.ONE.X_1=0; }

			if(IN_X26==1)
			{ Input_Port.ONE.X_2=1; }
			else
			{ Input_Port.ONE.X_2=0; }

			if(IN_X27==1)
			{ Input_Port.ONE.X_3=1; }
			else
			{ Input_Port.ONE.X_3=0; }

			if(IN_X28==1)
			{ Input_Port.ONE.X_4=1; }
			else
			{ Input_Port.ONE.X_4=0; }
			if(IN_X29==1)
			{ Input_Port.ONE.X_5=1; }
			else
			{ Input_Port.ONE.X_5=0; }

			if(IN_X30==1)
			{ Input_Port.ONE.X_6=1; }
			else
			{ Input_Port.ONE.X_6=0; }

			if(IN_X31==1)
			{ Input_Port.ONE.X_7=1; }
			else
			{ Input_Port.ONE.X_7=0; }

			if(IN_X32==1)
			{ Input_Port.ONE.X_8=1; }
			else
			{ Input_Port.ONE.X_8=0; }
		}

   return Input_Port.ALL;
}
   /*******************************************************************************
* Function Name  :
* Description    :读取输出状态
********************************************************************************/
unsigned int OutputRead(void)
{
	
		if(OUT_Y1==1)
		{ Output_Port.ONE.Y_1=1; }
		else
		{ Output_Port.ONE.Y_1=0; }

		if(OUT_Y2==1)
		{ Output_Port.ONE.Y_2=1; }
		else
		{ Output_Port.ONE.Y_2=0; }

		if(OUT_Y3==1)
		{ Output_Port.ONE.Y_3=1; }
		else
		{ Output_Port.ONE.Y_3=0; }

		if(OUT_Y4==1)
		{ Output_Port.ONE.Y_4=1; }
		else
		{ Output_Port.ONE.Y_4=0; }

		if(OUT_Y5==1)
		{ Output_Port.ONE.Y_5=1; }
		else
		{ Output_Port.ONE.Y_5=0; }

		if(OUT_Y6==1)
		{ Output_Port.ONE.Y_6=1; }
		else
		{ Output_Port.ONE.Y_6=0; }

		if(OUT_Y7==1)
		{ Output_Port.ONE.Y_7=1; }
		else
		{ Output_Port.ONE.Y_7=0; }

		if(OUT_Y8==1)
		{ Output_Port.ONE.Y_8=1; }
		else
		{ Output_Port.ONE.Y_8=0; }

		if(OUT_Y9==1)
		{ Output_Port.ONE.Y_9=1; }
		else
		{ Output_Port.ONE.Y_9=0;}


		if(OUT_Y10==1)
		{ Output_Port.ONE.Y_10=1; }
		else
		{ Output_Port.ONE.Y_10=0; }

		if(OUT_Y11==1)
		{ Output_Port.ONE.Y_11=1; }
		else
		{ Output_Port.ONE.Y_11=0; }

		if(OUT_Y12==1)
		{ Output_Port.ONE.Y_12=1; }
		else
		{ Output_Port.ONE.Y_12=0; }

		if(OUT_Y13==1)
		{ Output_Port.ONE.Y_13=1; }
		else
		{ Output_Port.ONE.Y_13=0; }

		if(OUT_Y14==1)
		{ Output_Port.ONE.Y_14=1; }
		else
		{ Output_Port.ONE.Y_14=0; }

		if(OUT_Y15==1)
		{ Output_Port.ONE.Y_15=1; }
		else
		{ Output_Port.ONE.Y_15=0; }

		if(OUT_Y16==1)
		{ Output_Port.ONE.Y_16=1; }
		else
		{ Output_Port.ONE.Y_16=0; }


    return ~Output_Port.ALL;
}

/*******************************************************************************
* Function Name  :
* Description    :
********************************************************************************/
int ReadTestboardID()
{
		Input_Port.ALL = 0;
		if(GPIO_ReadInputDataBit(GPIOD, GPIO_Pin_9)==0)
		{ Input_Port.ONE.X_1=1; }
		else
		{ Input_Port.ONE.X_1=0; }
		if(GPIO_ReadInputDataBit(GPIOD, GPIO_Pin_10)==0)
		{ Input_Port.ONE.X_2=1; }
		else
		{ Input_Port.ONE.X_2=0; }
		if(GPIO_ReadInputDataBit(GPIOD, GPIO_Pin_11)==0)
		{ Input_Port.ONE.X_3=1; }
		else
		{ Input_Port.ONE.X_3=0; }
		
		return Input_Port.ALL;
}
