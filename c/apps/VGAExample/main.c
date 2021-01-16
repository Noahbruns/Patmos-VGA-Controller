
#include <stdio.h>

#include "vga.h"

void gradientHorizontal(color from, color to);

int main() {
  fill((color){23, 92, 138});
  renderTextWhite(10, 10, "Patmos says: HELLO WORLD!");

  renderTextWhite(10, 60, 
  "root@patmos:~# \n");

  renderTextWhite(10, 200, 
    "Lorem ipsum dolor sit amet,\n"
    "consectetur adipiscing elit,\n"
    "sed do eiusmod tempor incididunt\n"
    "ut labore et dolore magna aliqua.\n"
    "Ullamcorper eget nulla facilisi\n"
    "etiam dignissim diam.\n"
    "Dictumst vestibulum rhoncus\n"
    "est pellentesque.\n"
    "Id volutpat lacus laoreet\n"
    "non curabitur.");

  /*for (int i = 0; i < VGA_DISPLAY_WIDTH; i++) {
    for (int j = 0; j < VGA_DISPLAY_HEIGHT; j++) {
      //writePixel(i, j, (color){255, 255, 255});

      if ((i / 100 + j / 100) % 2 == 0) {
        writePixel(i, j, (color){255, 255, 255});
      }
      else {
        writePixel(i, j, (color){0, 0, 0});
      }
    }
  }*/

  //gradientHorizontal((color){255, 255, 255}, (color){0, 0, 0});
  //gradientHorizontal((color){59, 29, 111}, (color){253, 174, 128});

  return 0;
}


void gradientHorizontal(color from, color to) {
  int R = to.R - from.R;
  int G = to.G - from.G;
  int B = to.B - from.B;

  for (int i = 0; i < VGA_DISPLAY_WIDTH; i++) {
    color c = plus(from, (color){(int)(R * i) / VGA_DISPLAY_WIDTH, (int)(G * i) / VGA_DISPLAY_WIDTH, (int)(B * i) / VGA_DISPLAY_WIDTH});

    for (int j = 0; j < VGA_DISPLAY_HEIGHT; j++) {
      writePixel(i, j, c);
    }
  }
}