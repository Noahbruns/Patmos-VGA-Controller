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
const color red = {true, false, false};
const color blue = {false, true, false};
const color green = {false, false, true};
const color yellow = {true, true, false};
const color cyan = {false, true, true};
const color magenta = {true, false, true};

volatile uint8_t (*base)[VGA_DISPLAY_HEIGHT][VGA_DISPLAY_WIDTH / 2] = 800000; //FIXME

void writePixel(uint16_t x, uint16_t y, color c) {
  uint8_t reset;
  uint8_t write = 0;

  if (x % 2 == 0) {
    reset = 0x0F;

    write |= c.R << 6;
    write |= c.G << 5;
    write |= c.B << 4;
  }
  else {
    reset = 0xF0;

    write |= c.R << 2;
    write |= c.G << 1;
    write |= c.B << 0;
  }

  (*base)[y][x / 2] &= reset;
  (*base)[y][x / 2] |= write;
}

void writePixelSafe(uint16_t x, uint16_t y, color c) {
  if (x <= VGA_DISPLAY_WIDTH && y <= VGA_DISPLAY_HEIGHT)
    writePixel(x, y, c);
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

void renderChar(uint16_t x, uint16_t y, color color, char c) {
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

void renderText(uint16_t x, uint16_t y, color color, char *text) {
  uint16_t org_x = x;

  for (int i = 0; text[i] != 0; i++) {
    if (text[i] == '\n') {
      y += FONT_LINEHEIGHT;
      x = org_x;
      continue;
    }

    renderChar(x, y, color, text[i]);
    x += FONT_WIDTH + FONT_SPACE;
  }
}

int line_y = 2;
void vgaprintf(const char * format, ...) {
  va_list arglist;

  line_y += 18;
  char buffer [100];

  snprintf(buffer, 100, format, arglist);
  renderText(20, line_y, white, buffer);
}

#define VGALib
#endif