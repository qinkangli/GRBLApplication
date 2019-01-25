#ifndef GUI_H_
#define GUI_H_

#include "stm32f10x.h"

 

u16 LCD_BGR2RGB(u16 c);
void Gui_Circle(u16 X,u16 Y,u16 R,u16 fc); 
void Gui_DrawLine(u16 x0, u16 y0,u16 x1, u16 y1,u16 Color);  
void Gui_box(u16 x, u16 y, u16 w, u16 h,u16 bc);
void Gui_box2(u16 x,u16 y,u16 w,u16 h, u8 mode);
void DisplayButtonDown(u16 x1,u16 y1,u16 x2,u16 y2);
void DisplayButtonDown2(u16 x1,u16 y1,u16 x2,u16 y2);
void DisapperButtonUp(u16 x1,u16 y1,u16 x2,u16 y2);
void DisplayButtonUp(u16 x1,u16 y1,u16 x2,u16 y2);
void Gui_DrawFont_GBK16(u16 x, u16 y, u16 fc, u16 bc, u8 *s);
void Gui_DrawFont_GBK24(u16 x, u16 y, u16 fc, u16 bc, u8 *s);

void LCD_ShowChar(u16 x,u16 y,u8 num,u8 size,u8 mode);
void LCD_ShowNum(u16 x,u16 y,u32 num,u8 len,u8 size);
void LCD_ShowString(u16 x,u16 y,u16 width,u16 height,u8 size,u8 *p);
void showimage(u8 x1,u8 y1,u8 x2,u8 y2,const unsigned char *p);
void showimage2(u8 x,u8 y,u8 height,u8 length,u16 fc,u16 bc,const unsigned char *s);
void LCD_ShowString(u16 x,u16 y,u16 width,u16 height,u8 size,u8 *p);


#endif


