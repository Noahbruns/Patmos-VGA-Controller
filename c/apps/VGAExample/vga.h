/*
Libary for writing image to VGA Buffer
*/

#ifndef VGALib

#include <stdint.h>
#include <stdbool.h>

#include "ascii_8x13.h"
#define FONT_WIDTH 8
#define FONT_HEIGHT 13
#define FONT_SPACE 3
#define FONT_LINEHEIGHT 16

#define VGA_DISPLAY_WIDTH  800
#define VGA_DISPLAY_HEIGHT 600

typedef struct {
    bool R;
    bool G;
    bool B;
} color;

const color black = {0, 0, 0};
const color white = {true, true, true};

volatile uint8_t (*base)[VGA_DISPLAY_HEIGHT][VGA_DISPLAY_WIDTH / 2] = 800000; //FIXME

void writePixel(uint16_t x, uint16_t y, color c) {
    uint8_t reset;
    uint8_t write = 0;

    if (x % 2 == 0) {
      reset = 0xF0;

      write |= c.R << 8;
      write |= c.G << 7;
      write |= c.B << 6;
    }
    else {
      reset = 0x0F;

      write |= c.R << 4;
      write |= c.G << 3;
      write |= c.B << 2;
    }

    (*base)[y][x / 2] &= reset;
    (*base)[y][x / 2] |= write;
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