
#include <stdio.h>

#include "vga.h"

int main() {

  for (int i = 0; i < VGA_DISPLAY_WIDTH; i++) {
    for (int j = 0; j < VGA_DISPLAY_HEIGHT; j++) {
      //writePixel(i, j, (color){255, 255, 255});

      if ((i / 100 + j / 100) % 2 == 0) {
        writePixel(i, j, (color){255, 255, 255});
      }
      else {
        writePixel(i, j, (color){0, 0, 0});
      }
    }
  }

  return 0;
}
