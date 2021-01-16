/*
Libary for writing image to VGA Buffer
*/

#ifndef VGALib

#include <stdint.h>

#include "ascii_8x13.h"
#define FONT_WIDTH 8
#define FONT_HEIGHT 13
#define FONT_SPACE 3
#define FONT_LINEHEIGHT 16

#define VGA_DISPLAY_WIDTH  800
#define VGA_DISPLAY_HEIGHT 600

typedef struct {
    uint8_t R;
    uint8_t G;
    uint8_t B;
} color;

const color black = {0, 0, 0};
const color white = {255, 255, 255};

volatile uint16_t (*base)[VGA_DISPLAY_HEIGHT][VGA_DISPLAY_WIDTH] = 800000; //FIXME

void writePixel(uint16_t x, uint16_t y, color c) {
    uint16_t temp = 0;
    temp |= (c.R >> 3) << 10;
    temp |= (c.G >> 3) << 5;
    temp |= (c.B >> 3);

    (*base)[y][x] = temp;
}

color plus(color A, color B) {
  return (color){A.R + B.R, A.G + B.G, A.B + B.B};
}

void fill(color c) {
  for (int i = 0; i < VGA_DISPLAY_WIDTH; i++) {
    for (int j = 0; j < VGA_DISPLAY_HEIGHT; j++) {
      writePixel(i, j, c);
    }
  }
}

void blank() {
  fill(black);
}

void renderChar(uint16_t x, uint16_t y, char c, color color) {
  for (int i = 0; i < FONT_HEIGHT; i++) {
    for (int j = 0; j < FONT_WIDTH; j++) {
      if (c <= 32 || c > 127)
        continue;

      char t = ascii_8x13_font[c - 33][i];
      if ((t >> j) & 1)
        writePixel(x + (FONT_WIDTH - j), y + (FONT_HEIGHT - i), color);
    }
  }
}

void renderCharWhite(uint16_t x, uint16_t y, char c) {
  return renderChar(x, y, c, white);
}

void renderText(uint16_t x, uint16_t y, char *text, color color) {
  uint16_t org_x = x;

  for (int i = 0; text[i] != 0; i++) {
    if (text[i] == '\n') {
      y += FONT_LINEHEIGHT;
      x = org_x;
      continue;
    }

    renderChar(x, y, text[i], color);
    x += FONT_WIDTH + FONT_SPACE;
  }
}

void renderTextWhite(uint16_t x, uint16_t y, char *text) {
  return renderText(x, y, text, white);
}

#define VGALib
#endif