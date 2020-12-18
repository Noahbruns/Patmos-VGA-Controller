
#include <stdio.h>

#include "vga.h"

int main() {

  for (int i = 0; i < VGA_DISPLAY_WIDTH; i++) {
    for (int j = 0; j < VGA_DISPLAY_HEIGHT; j++) {
      writePixel(i, j, (color){255 * i / VGA_DISPLAY_WIDTH,255 * j / VGA_DISPLAY_HEIGHT,255});
    }
  }

  return 0;
}
