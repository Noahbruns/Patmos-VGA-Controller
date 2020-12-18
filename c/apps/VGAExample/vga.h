/*
Libary for writing image to VGA Buffer
*/

#ifndef VGALib

#include <stdint.h>

#define VGA_DISPLAY_WIDTH  800
#define VGA_DISPLAY_HEIGHT 600

typedef struct {
    uint8_t R;
    uint8_t G;
    uint8_t B;
} color;

volatile uint16_t (*base)[VGA_DISPLAY_WIDTH][VGA_DISPLAY_HEIGHT] = 0x0;

void writePixel(uint16_t x, uint16_t y, color c) {
    uint16_t temp = 0;
    temp |= (c.R >> 3) << 10;
    temp |= (c.G >> 3) << 5;
    temp |= (c.B >> 3);

    (*base)[x][y] = temp;
}

#define VGALib
#endif