#include "USART3_init.h"


UsartRxTypeDef3 USARTStructure3;
unsigned char USART3_TX_BUFF[USART3_TX_BUFF_SIZEMAX];   

/****************************************************************************
* 名	称：void USART3_GPIO_Init(void)
* 功	能：串口引脚初始化
* 入口参数：无
* 出口参数：无
* 说	明：无
****************************************************************************/
void USART3_GPIO_Init(void)			//串口引脚初始化
{
	GPIO_InitTypeDef GPIO_InitStructure;		//串口引脚结构
	
	//串口引脚分配时钟
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOB, ENABLE);

	//配置串口 Tx (PB.10) 为复用推挽输出
  	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_10;					//串口发送引脚
  	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AF_PP;				//复用推挽输出
  	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;			//频率50MHz
  	GPIO_Init(GPIOB, &GPIO_InitStructure);						//初始化引脚
    
	// 配置串口 Rx (PB.11) 为浮空输入
  	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_11;					//串口接收引脚
  	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IN_FLOATING;		//浮空输入
  	GPIO_Init(GPIOB, &GPIO_InitStructure);						//初始化引脚
												 
}

/****************************************************************************
* 名	称：void USART3_Init(void)
* 功	能：串口初始化
* 入口参数：无
* 出口参数：无
* 说	明：无
****************************************************************************/
void USART3_Init(void)
{		
	USART_InitTypeDef UART_InitStructure;		//串口结构

	//串口分配时钟
	RCC_APB1PeriphClockCmd(RCC_APB1Periph_USART3, ENABLE);

	//串口初始化
	UART_InitStructure.USART_BaudRate            = USART3BaudRate;	//波特率
	UART_InitStructure.USART_WordLength          = USART_WordLength_8b;		//数据位8bit
	UART_InitStructure.USART_StopBits            = USART_StopBits_1;		//停止位个数
	UART_InitStructure.USART_Parity              = USART_Parity_No ;		//不进行奇偶效验
	UART_InitStructure.USART_HardwareFlowControl = USART_HardwareFlowControl_None;	//RTS和CTS使能(None不使用)
	UART_InitStructure.USART_Mode                = USART_Mode_Rx | USART_Mode_Tx;	//发送和接收使能
	USART_Init(USART3, &UART_InitStructure);	//初始化串口
}

/****************************************************************************
* 名	称：void USART2_NVIC_Init(void)
* 功	能：串口中断向量表初始化
* 入口参数：无
* 出口参数：无
* 说	明：无
****************************************************************************/
void USART3_NVIC_Init(void)
{
	NVIC_InitTypeDef NVIC_InitStructure; 		//中断控制器变量

	NVIC_InitStructure.NVIC_IRQChannel = USART3_IRQn;			//设置中断通道
	NVIC_InitStructure.NVIC_IRQChannelPreemptionPriority = 0;	//主优先级设置
	NVIC_InitStructure.NVIC_IRQChannelSubPriority = 2;			//设置优先级
	NVIC_InitStructure.NVIC_IRQChannelCmd = ENABLE;				//打开串口中断
	NVIC_Init(&NVIC_InitStructure);								//初始化中断向量表
}

/****************************************************************************
* 名	称：void USART2_DMATxd_Init(void)
* 功	能：串口DMA初始化
* 入口参数：无
* 出口参数：无
* 说	明：无
****************************************************************************/
void USART3_DMATxd_Init(void)
{
	NVIC_InitTypeDef NVIC_InitStructure; 		//中断控制器变量
	DMA_InitTypeDef DMA_InitStructure;			//DMA结构

	RCC_AHBPeriphClockCmd(RCC_APB1Periph_TIM3, ENABLE);			//使能DMA1时钟

	//DMA中断向量配置
	NVIC_InitStructure.NVIC_IRQChannel = DMA1_Channel2_IRQn;	//设置DMA中断通道
	NVIC_InitStructure.NVIC_IRQChannelPreemptionPriority = 0;	//主优先级设置
	NVIC_InitStructure.NVIC_IRQChannelSubPriority = 3;			//设置优先级
	NVIC_InitStructure.NVIC_IRQChannelCmd = ENABLE;				//打开中断
	NVIC_Init(&NVIC_InitStructure); 

	//DMA配置
	DMA_DeInit(DMA1_Channel2);  		   									//复位DMA1_Channel4通道为默认值
	DMA_InitStructure.DMA_PeripheralBaseAddr = USART3_BASE + 4;				//DMA通道外设基地址
	DMA_InitStructure.DMA_MemoryBaseAddr = (u32)USART3_TX_BUFF;				//DMA通道存储器基地址
	DMA_InitStructure.DMA_DIR = DMA_DIR_PeripheralDST;						//DMA目的地	(DMA_DIR_PeripheralSRC源)
	DMA_InitStructure.DMA_PeripheralInc = DMA_PeripheralInc_Disable;		//当前外设寄存器不变
	DMA_InitStructure.DMA_MemoryInc = DMA_MemoryInc_Enable;					//当前存储寄存器增加
	DMA_InitStructure.DMA_PeripheralDataSize = DMA_PeripheralDataSize_Byte;	//外设数据宽度为字节(8位)
	DMA_InitStructure.DMA_MemoryDataSize = DMA_MemoryDataSize_Byte;			//存储器数据宽度为字节(8位)
	DMA_InitStructure.DMA_Mode = DMA_Mode_Normal;							//正常缓冲模式
	DMA_InitStructure.DMA_Priority = DMA_Priority_VeryHigh;					//DMA通道优先级非常高
	DMA_InitStructure.DMA_M2M = DMA_M2M_Disable;							//DMA通道未配置存储器到存储器传输
	DMA_Init(DMA1_Channel2, &DMA_InitStructure);							//根据上诉设置初始化DMA
	DMA_ITConfig(DMA1_Channel2, DMA_IT_TC, ENABLE);    						//开启DMA通道中断
}

/****************************************************************************
* 名	称：void USART2_Config(void)
* 功	能：串口设置
* 入口参数：无
* 出口参数：无
* 说	明：默认为包数据接收					 
****************************************************************************/
void USART3_Config(void)
{
	USART3_Init();				//串口初始化
	USART3_GPIO_Init();			//串口引脚初始化
	USART3_NVIC_Init();			//中断初始化
	USART3_DMATxd_Init();		//DMA发送初始化
	USART3_RX_Buffer_Clear();	//接收中断与接收缓冲区绑定
	TIM4_Config();
	TIM4_NVIC_Init();
		
	USART_ClearITPendingBit(USART3, USART_IT_RXNE);				//清接收标志
	USART_ITConfig(USART3, USART_IT_RXNE, ENABLE);				//开启接收中断
	
	USART_Cmd(USART3, ENABLE);  								//使能失能串口外设	
}


/****************************************************************************
* 名	称:
* 功	能：接收超时检测
* 入口参数：无
* 出口参数：无
* 说	明：	
//TIM_TimeBaseStructure.TIM_Prescaler = 7199;//时钟预分频数  
//TIM_TimeBaseStructure.TIM_Period = 9999; // 自动重装载寄存器周期的值(定时时间) 
//((1+TIM_Prescaler )/72M)*(1+TIM_Period )
//((1+7199)/72M)*(1+9999)=72/72M *10000=1 秒
// Period =9  ,Prescaler = 7199 定时1ms
// Period =99  ,Prescaler = 7199 定时10ms
****************************************************************************/
void TIM4_Config(void)
{

	  TIM_TimeBaseInitTypeDef  TIM_TimeBaseStructure;
	
		RCC_APB1PeriphClockCmd(RCC_APB1Periph_TIM3, ENABLE);//使能定时器3
	
    TIM_TimeBaseStructure.TIM_Period =49;          		  //设置定时器重载值,计算时应加1
    TIM_TimeBaseStructure.TIM_Prescaler = 7199;       		  //定时器时钟预分频,计算时应加1  定时5ms
    TIM_TimeBaseStructure.TIM_ClockDivision = 0x0;    		  //时钟分割，用于滤波器
    TIM_TimeBaseStructure.TIM_CounterMode = TIM_CounterMode_Up; //选择向上计数模式 
    TIM_TimeBaseStructure.TIM_RepetitionCounter = 0x00;		  //重载次数计数器，计数到0便产生中断事件
    TIM_TimeBaseInit(TIM4, &TIM_TimeBaseStructure);			  //以上参数初始化TIM3

    TIM_ClearFlag(TIM4, TIM_FLAG_Update);					  		//清除更新事件标志位(防止启动便中断)
    TIM_ITConfig(TIM4, TIM_IT_Update, ENABLE);				  //打开重载更新中断 

    TIM_Cmd(TIM4, DISABLE);									  					//使能TIM3	
}

/****************************************************************************
* 名	称：
* 功	能：
* 入口参数：
* 出口参数：无
* 说	明：无				   
****************************************************************************/
void TIM4_NVIC_Init(void)
{
	NVIC_InitTypeDef NVIC_InitStructure; 		//中断控制器变量

  NVIC_InitStructure.NVIC_IRQChannel = TIM4_IRQn;			     //选择定时器2中断线
  NVIC_InitStructure.NVIC_IRQChannelPreemptionPriority = 0;		 //抢占优先级为1
  NVIC_InitStructure.NVIC_IRQChannelSubPriority = 4;			 //响应优先级为0
  NVIC_InitStructure.NVIC_IRQChannelCmd = ENABLE;				 //使能中断通道
  NVIC_Init(&NVIC_InitStructure);								 //用以上参数初始化10-15号线中断通道
}

/****************************************************************************
* 名	称：void USART2_SendByte(u8 Data)
* 功	能：单字符发送
* 入口参数：Data 	发送单字符数据
* 出口参数：无
* 说	明：无				   
****************************************************************************/
void USART3_RX_Buffer_Clear(void)	
{

	USARTStructure3.RX_Len = 0;
		
}
/****************************************************************************
* 名	称：void USART2_SendByte(u8 Data)
* 功	能：单字符发送
* 入口参数：Data 	发送单字符数据
* 出口参数：无
* 说	明：无				   
****************************************************************************/
void USART3_SendByte(u8 Data)		   //单字符数据输出
{
	USART_SendData(USART3, Data);
    while(USART_GetFlagStatus(USART3, USART_FLAG_TXE) == RESET);
}

/****************************************************************************
* 名	称：void USART2_SendString(u8* Data,u32 Len)
* 功	能：多字符输出
* 入口参数：Data 	输出的单字符数据
			Len		字符个数
* 出口参数：无
* 说	明：无					 
****************************************************************************/
void USART3_SendString(char * Data,u32 Len)		   //多字符输出
{
	u32 i=0;

	for(i=0;i<Len;i++)
  {    
		USART_SendData(USART3, Data[i]);
        while(USART_GetFlagStatus(USART3, USART_FLAG_TXE) == RESET);
	}

}

/****************************************************************************
* 名	称：void USART2_DMASendString(u8* Data,u32 Len)
* 功	能：DMA方式多字符输出
* 入口参数：Data 	输出的单字符数据
			Len		字符个数
* 出口参数：无
* 说	明：必须在USART2_DMATxd_Init初始化之后才能使用
			DMA无需CPU发送 和 用CPU发送 会有发送冲突	 
****************************************************************************/
void USART3_DMASendData(char * Data,u32 Len)		   //多字符输出
{
	
	memcpy(USART3_TX_BUFF, Data, Len);			   //拷贝数据到发送缓冲区
    DMA1_Channel2->CNDTR = Len;					   //发送字节数量
	USART_DMACmd(USART3, USART_DMAReq_Tx, ENABLE); //开启
	DMA_Cmd(DMA1_Channel2, ENABLE);				   //始能DMA通道
}

/****************************************************************************
* 名	称：void USART2_IRQHandler(void)	
* 功	能：中断机制
* 入口参数：无
* 出口参数：无
* 说	明：接收到的数据存入接收缓冲区
	USART_GetITStatus		检查指定的USART中断发生与否
	USART_GetFlagStatus	检查指定的USART标志位设置与否
****************************************************************************/
void USART3_IRQHandler(void)
{
	unsigned char i;
	unsigned char USART3_GetChar;
	
	if(USART_GetITStatus(USART3, USART_IT_RXNE) == SET)				// 串口接收数据触发中断
	{
		USART_ClearITPendingBit(USART3, USART_IT_RXNE);					//清空接收中断标志			//接收到的字符数据

		USART3_GetChar  = USART_ReceiveData(USART3);
		
    TIM_Cmd(TIM4, ENABLE);									  					 		//使能TIM4	
		TIM_SetCounter(TIM4, 0);																//清当前的计数
	
		if(USART3_GetChar==0xA)                             		//接收到LF结束符
		{

		}
		else if(USART3_GetChar==0xD)                        		//接收到CR结束符
		{
			USARTStructure3.RX_BUFF[USARTStructure3.RX_Len] = '\0';
			USARTStructure3.RX_Flag = 1;
			TIM_Cmd(TIM4, DISABLE);									  						//关闭 TIM4	
		}
		else
		{
				USARTStructure3.RX_BUFF[USARTStructure3.RX_Len]=USART3_GetChar;
				USARTStructure3.RX_Len++;
				if(USARTStructure3.RX_Len>=60)
				{
						for(i=0;i<60;i++)
						{
							 USARTStructure3.RX_BUFF[i]=0x00;  						//清空字符串
						}
						USARTStructure3.RX_Len=0;
				}
		}
	 }

	else if(USART_GetFlagStatus(USART3, USART_IT_ORE) == SET)	//检测是否有接收溢出
	{
		USART_ReceiveData(USART3);															//清接收溢出标志，只能用读数据的方式来清溢出标志
	}

	else if(USART_GetITStatus(USART3, USART_IT_TXE) == SET)		//串口发送数据触发中断
	{
		
	}
}
/*******************************************************************************
* Function Name  : TIM2_IRQHandler	 定时5ms
* Description    : This function handles TIM2 global interrupt request.
* Input          : None
* Output         : None
* Return         : None
*******************************************************************************/
void TIM4_IRQHandler(void)
{
  /* 清除中断标志位 */
//	unsigned int rlen;
		
  TIM_ClearITPendingBit(TIM4, TIM_IT_Update);
	
//	for(rlen = 0 ;rlen<USARTStructure2.RX_TMEP_Len; rlen++ )
//	{
//			USARTStructure2.RX_BUFF[rlen]=USARTStructure2.RX_TEMP_BUFF[rlen];
//	}
//	USARTStructure2.RX_Len = USARTStructure2.RX_TMEP_Len;
//	USARTStructure2.RX_TMEP_Len = 0;
//	USARTStructure3.RX_BUFF[USARTStructure3.RX_Len] = '\0';
//	USARTStructure3.RX_Flag=1; 
	USARTStructure3.RX_Len = 0;
	
	TIM_Cmd(TIM4, DISABLE);									  			//关闭 TIM4	

}

/****************************************************************************
* 名	称：void DMA1_Channel2_IRQHandler(void)	
* 功	能：DMA中断机制
* 入口参数：无
* 出口参数：无
* 说	明：无
****************************************************************************/
void DMA1_Channel2_IRQHandler(void)
{
	if(DMA_GetFlagStatus(DMA1_FLAG_TC2)) //如果发送完成
	{
		USART_DMACmd(USART2, USART_DMAReq_Tx, DISABLE);    //关闭DMA发送
		DMA_Cmd(DMA1_Channel2, DISABLE);	       			//关闭DMA通道  	

	}

	DMA_ClearFlag(DMA1_FLAG_GL2| DMA1_FLAG_TC2 | DMA1_FLAG_HT2 | DMA1_FLAG_TE2);  //清除DMA相关标志
}


