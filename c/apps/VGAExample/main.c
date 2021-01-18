
#include <stdio.h>

#include "vga.h"

void gradientHorizontal(color from, color to);

int main() {
  fill(black);

  renderText(10, 10, white, "Patmos says: HELLO WORLD!");

  renderText(10, 60, white,
  "root@patmos:~# \n");

  
  renderText(10, 60, white,
  "               _\n");

  renderText(10, 200, white,
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

  return 0;
}


void chess() {
  for (int i = 0; i < VGA_DISPLAY_WIDTH; i++) {
    for (int j = 0; j < VGA_DISPLAY_HEIGHT; j++) {
      if ((i / 100 + j / 100) % 2 == 0) {
        writePixel(i, j, white);
      }
      else {
        writePixel(i, j, black);
      }
    }
  }
}