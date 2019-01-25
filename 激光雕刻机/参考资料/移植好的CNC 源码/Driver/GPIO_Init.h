#ifndef _GPIO_INIT_H_	
#define _GPIO_INIT_H_

#ifdef __cplusplus			//定义对CPP进行C处理
extern "C" {
#endif

#include "Config.h"

#define ON  0
#define OFF 1
	
#define IN_X1			GPIO_ReadInputDataBit(GPIOE, GPIO_Pin_2)
#define IN_X2			GPIO_ReadInputDataBit(GPIOE, GPIO_Pin_3)
#define IN_X3			GPIO_ReadInputDataBit(GPIOE, GPIO_Pin_4)
#define IN_X4			GPIO_ReadInputDataBit(GPIOE, GPIO_Pin_5)
#define IN_X5			GPIO_ReadInputDataBit(GPIOE, GPIO_Pin_6)
#define IN_X6			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_0)
#define IN_X7			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_1)
#define IN_X8			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_2)
	
#define IN_X9			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_3)
#define IN_X10			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_4)
#define IN_X11			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_5)
#define IN_X12			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_6)
#define IN_X13			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_7)
#define IN_X14			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_8)
#define IN_X15			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_9)
#define IN_X16			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_10)

#define IN_X17			GPIO_ReadInputDataBit(GPIOB, GPIO_Pin_0)
#define IN_X18			GPIO_ReadInputDataBit(GPIOB, GPIO_Pin_1)
#define IN_X19			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_11)
#define IN_X20			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_12)
#define IN_X21			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_13)
#define IN_X22			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_14)
#define IN_X23			GPIO_ReadInputDataBit(GPIOF, GPIO_Pin_15)
#define IN_X24			GPIO_ReadInputDataBit(GPIOG, GPIO_Pin_0)

#define IN_X25			GPIO_ReadInputDataBit(GPIOG, GPIO_Pin_1)
#define IN_X26			GPIO_ReadInputDataBit(GPIOE, GPIO_Pin_7)
#define IN_X27			GPIO_ReadInputDataBit(GPIOE, GPIO_Pin_8)
#define IN_X28			GPIO_ReadInputDataBit(GPIOE, GPIO_Pin_9)
#define IN_X29			GPIO_ReadInputDataBit(GPIOE, GPIO_Pin_10)
#define IN_X30			GPIO_ReadInputDataBit(GPIOE, GPIO_Pin_11)
#define IN_X31			GPIO_ReadInputDataBit(GPIOE, GPIO_Pin_12)
#define IN_X32			GPIO_ReadInputDataBit(GPIOE, GPIO_Pin_13)


#define OUT_Y1        GPIO_ReadInputDataBit(GPIOD, GPIO_Pin_14)
#define OUT_Y1_ON     GPIO_ResetBits(GPIOD, GPIO_Pin_14)
#define OUT_Y1_OFF    GPIO_SetBits(GPIOD, GPIO_Pin_14)
#define OUT_Y2        GPIO_ReadInputDataBit(GPIOD, GPIO_Pin_15)
#define OUT_Y2_ON     GPIO_ResetBits(GPIOD, GPIO_Pin_15)
#define OUT_Y2_OFF    GPIO_SetBits(GPIOD, GPIO_Pin_15)
#define OUT_Y3        GPIO_ReadInputDataBit(GPIOG, GPIO_Pin_2)
#define OUT_Y3_ON     GPIO_ResetBits(GPIOG, GPIO_Pin_2)
#define OUT_Y3_OFF    GPIO_SetBits(GPIOG, GPIO_Pin_2)
#define OUT_Y4        GPIO_ReadInputDataBit(GPIOG, GPIO_Pin_3)
#define OUT_Y4_ON     GPIO_ResetBits(GPIOG, GPIO_Pin_3)
#define OUT_Y4_OFF    GPIO_SetBits(GPIOG, GPIO_Pin_3)
#define OUT_Y5        GPIO_ReadInputDataBit(GPIOC, GPIO_Pin_7)
#define OUT_Y5_ON     GPIO_ResetBits(GPIOC, GPIO_Pin_7)
#define OUT_Y5_OFF    GPIO_SetBits(GPIOC, GPIO_Pin_7)
#define OUT_Y6        GPIO_ReadInputDataBit(GPIOC, GPIO_Pin_8)
#define OUT_Y6_ON     GPIO_ResetBits(GPIOC, GPIO_Pin_8)
#define OUT_Y6_OFF    GPIO_SetBits(GPIOC, GPIO_Pin_8)
#define OUT_Y7        GPIO_ReadInputDataBit(GPIOC, GPIO_Pin_9)
#define OUT_Y7_ON     GPIO_ResetBits(GPIOC, GPIO_Pin_9)
#define OUT_Y7_OFF    GPIO_SetBits(GPIOC, GPIO_Pin_9)
#define OUT_Y8        GPIO_ReadInputDataBit(GPIOA, GPIO_Pin_8)
#define OUT_Y8_ON     GPIO_ResetBits(GPIOA, GPIO_Pin_8)
#define OUT_Y8_OFF    GPIO_SetBits(GPIOA, GPIO_Pin_8)


#define OUT_Y9       GPIO_ReadInputDataBit(GPIOC, GPIO_Pin_6)
#define OUT_Y9_ON    GPIO_ResetBits(GPIOC, GPIO_Pin_6)
#define OUT_Y9_OFF   GPIO_SetBits(GPIOC, GPIO_Pin_6)
#define OUT_Y10       GPIO_ReadInputDataBit(GPIOG, GPIO_Pin_6)
#define OUT_Y10_ON    GPIO_ResetBits(GPIOG, GPIO_Pin_6)
#define OUT_Y10_OFF   GPIO_SetBits(GPIOG, GPIO_Pin_6)
#define OUT_Y11       GPIO_ReadInputDataBit(GPIOG, GPIO_Pin_5)
#define OUT_Y11_ON    GPIO_ResetBits(GPIOG, GPIO_Pin_5)
#define OUT_Y11_OFF   GPIO_SetBits(GPIOG, GPIO_Pin_5)
#define OUT_Y12        GPIO_ReadInputDataBit(GPIOG, GPIO_Pin_4)
#define OUT_Y12_ON     GPIO_ResetBits(GPIOG, GPIO_Pin_4)
#define OUT_Y12_OFF    GPIO_SetBits(GPIOG, GPIO_Pin_4)

#define OUT_Y13       GPIO_ReadInputDataBit(GPIOG, GPIO_Pin_14)
#define OUT_Y13_ON    GPIO_ResetBits(GPIOG, GPIO_Pin_14)
#define OUT_Y13_OFF   GPIO_SetBits(GPIOG, GPIO_Pin_14)
#define OUT_Y14       GPIO_ReadInputDataBit(GPIOG, GPIO_Pin_13)
#define OUT_Y14_ON    GPIO_ResetBits(GPIOG, GPIO_Pin_13)
#define OUT_Y14_OFF   GPIO_SetBits(GPIOG, GPIO_Pin_13)
#define OUT_Y15       GPIO_ReadInputDataBit(GPIOG, GPIO_Pin_12)
#define OUT_Y15_ON    GPIO_ResetBits(GPIOG, GPIO_Pin_12)
#define OUT_Y15_OFF   GPIO_SetBits(GPIOG, GPIO_Pin_12)
#define OUT_Y16       GPIO_ReadInputDataBit(GPIOD, GPIO_Pin_7)
#define OUT_Y16_ON    GPIO_ResetBits(GPIOD, GPIO_Pin_7)
#define OUT_Y16_OFF   GPIO_SetBits(GPIOD, GPIO_Pin_7)

#define PWR_ON    GPIO_SetBits(GPIOE, GPIO_Pin_14);GPIO_ResetBits(GPIOE, GPIO_Pin_15)
#define PWR_OFF   GPIO_ResetBits(GPIOE, GPIO_Pin_14);GPIO_SetBits(GPIOE, GPIO_Pin_15)


typedef union
{
    struct
    {
        unsigned X_1   : 1;
        unsigned X_2   : 1;
        unsigned X_3   : 1;
        unsigned X_4   : 1;
        unsigned X_5   : 1;
        unsigned X_6   : 1;
        unsigned X_7   : 1;
        unsigned X_8   : 1;

//			unsigned X_9   : 1;
//			unsigned X_10   : 1;
//			unsigned X_11   : 1;
//			unsigned X_12   : 1;
//			unsigned X_13   : 1;
//			unsigned X_14   : 1;
//			unsigned X_15   : 1;
//			unsigned X_16   : 1;
//			
//				unsigned X_17   : 1;
//        unsigned X_18   : 1;
//        unsigned X_19   : 1;
//        unsigned X_20   : 1;
//        unsigned X_21   : 1;
//        unsigned X_22   : 1;
//        unsigned X_23   : 1;
//        unsigned X_24   : 1;
//				
//				unsigned X_25   : 1;
//        unsigned X_26   : 1;
//        unsigned X_27   : 1;
//        unsigned X_28   : 1;
//        unsigned X_29   : 1;
//        unsigned X_30   : 1;
//        unsigned X_31   : 1;
//        unsigned X_32   : 1;
    }ONE;
    unsigned char ALL;
}Type_Input_Port;

extern Type_Input_Port Input_Port;

typedef union
{
    struct
    {
        unsigned Y_1   : 1;
        unsigned Y_2   : 1;
        unsigned Y_3   : 1;
        unsigned Y_4   : 1;
        unsigned Y_5   : 1;
        unsigned Y_6   : 1;
        unsigned Y_7   : 1;
        unsigned Y_8   : 1;

        unsigned Y_9   : 1;
        unsigned Y_10   : 1;
        unsigned Y_11   : 1;
        unsigned Y_12   : 1;
        unsigned Y_13   : 1;
        unsigned Y_14   : 1;
        unsigned Y_15   : 1;
        unsigned Y_16   : 1;
    }ONE;
    unsigned short ALL;
}Type_Output_Port;

extern Type_Output_Port Output_Port;

void GPIO_Config(void);
void OutputControl(unsigned int stats);
unsigned char InputRead(unsigned char ch);
unsigned int OutputRead(void);
int ReadTestboardID(void);

#ifdef __cplusplus		  
}
#endif

#endif
