
#include <stdio.h>
#include <stdlib.h>

#include "vga.h"
#include <math.h>

#include <machine/patmos.h>
#include <machine/exceptions.h>
#include <machine/rtc.h>
#include <machine/boot.h>

void Animation();
void text();
void chess();
void cpuinfo();
void snake();
void pacman();
void runPacman(int X, int Y, int toX, int toY);

int main() {
  fill(black);

  while (true) {Animation();}

  return 0;
}

void delay(int ms) {
  volatile int cnt=0;
  int a = ms * 2000;
  while (cnt<a){
    cnt++;
  }
}

void Animation() {
  fill(black);
  renderText(10, 20, white, "Loading");

  int x = 10 + 7 * (FONT_WIDTH + FONT_SPACE) + FONT_SPACE;

  for (int i = 0; i < 20; i++)
  {
    renderText(x, 20, white, ".");
    x += (FONT_WIDTH + FONT_SPACE);

    delay(250);
  }

  fill(black);
  cpuinfo();

  delay(3000);

  chess(white, black);
  delay(1000);
  chess(red, black);
  delay(1000);
  chess(red, green);
  delay(1000);
  chess(blue, green);
  delay(1000);
  chess(magenta, yellow);
  delay(1000);
  chess(yellow, black);

  delay(1000);

  pacman();
}

void runPacman(int X, int Y, int toX, int toY) {
  X -= 5;
  Y -= 5;
  toX -= 5;
  toY -= 5;

  for (int i = 0; i < 10; i++) {
    for (int j = 0; j < 10; j++) {
      writePixel(X + i, Y + j, red);
    }
  }

  int dirX = toX > X ? 2 : -2;

  while(X != toX) {
    if (dirX > 0) {
      for (int i = 0; i < 10; i++) {
        writePixel(X, Y + i, black);
        writePixel(X + 1, Y + i, black);
      }
      for (int i = 0; i < 10; i++) {
        writePixel(X + 10, Y + i, red);
        writePixel(X + 11, Y + i, red);
      }
    }
    else {
      for (int i = 0; i < 10; i++) {
        writePixel(X - 2, Y + i, red);
        writePixel(X - 1, Y + i, red);
      }
      for (int i = 0; i < 10; i++) {
        writePixel(X + 8, Y + i, black);
        writePixel(X + 9, Y + i, black);
      }
    }
    delay(5);
    X += dirX;
  }

  int dirY = toY > Y ? 2 : -2;

  while(Y != toY) {
    if (dirY > 0) {
      for (int i = 0; i < 10; i++) {
        writePixel(X + i, Y, black);
        writePixel(X + i, Y + 1, black);
      }
      for (int i = 0; i < 10; i++) {
        writePixel(X + i, Y + 10, red);
        writePixel(X + i, Y + 11, red);
      }
    }
    else {
      for (int i = 0; i < 10; i++) {
        writePixel(X + i, Y - 2, red);
        writePixel(X + i, Y - 1, red);
      }
      for (int i = 0; i < 10; i++) {
        writePixel(X + i, Y + 8, black);
        writePixel(X + i, Y + 9, black);
      }
    }
    delay(5);
    Y += dirY;
  }
  
  for (int i = -5; i < 20; i++) {
    for (int j = -5; j < 20; j++) {
      writePixel(X + i, Y + j, yellow);
    }
  }

  delay(200);

  for (int i = -5; i < 20; i++) {
    for (int j = -5; j < 20; j++) {
      writePixel(X + i, Y + j, black);
    }
  }

  for (int i = 0; i < 10; i++) {
    for (int j = 0; j < 10; j++) {
      writePixel(X + i, Y + j, red);
    }
  }
}

//Prints a cross representing the packages
void renderPackage(int x, int y, color c) {
  int sizeX = 10;

  for (int i = 0; i < sizeX; i++) {
    writePixel(x + i, y + i, c);
    writePixel(x + i, y - i, c);
    writePixel(x - i, y + i, c);
    writePixel(x - i, y - i, c);
  }
}

void pacman() {
  fill(black);

  int x = 20;
  int y = 300;

  int num_packages = 10;

  for (int i = 0; i < num_packages; i++) {
    //generate random position
    int xrand = (rand() % VGA_DISPLAY_WIDTH/2) * 2;
    int yrand = (rand() % VGA_DISPLAY_HEIGHT/2) * 2;
    renderPackage(xrand, yrand, magenta);

    runPacman(x,y, xrand, yrand);
    x = xrand;
    y = yrand;
  }
}

void text() {
  renderText(10, 10, cyan, "Patmos says: HELLO WORLD!");

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
}

void prefix(int size, char* buf)  __attribute__((section(".text.spm")));
void prefix(int size, char* buf){
  int pref = 0;
  while (size > 1024){
    size = size >> 10;
    pref++;
  }
  char* format;
  switch(pref){
    case 0:
      format = "%d B";
      break;
    case 1:
      format = "%d KB";
      break;
    case 2:
      format = "%d MB";
      break;
    case 3:
      format = "%d GB";
      break;
  }
  snprintf(buf,11,format,size);
  return;
}

void cpuinfo() {
  line_y = 2;

  vgaprintf("CPUINFO:\n");
  vgaprintf("get_cpu_cycles(): %d\n", get_cpu_cycles());
  vgaprintf("get_cpufeat(): %08x\n", get_cpufeat());
  char buf[12];
  int size;
  size = get_extmem_size();
  prefix(size,buf);
  vgaprintf("get_extmem_size(): %s\n", buf);
  vgaprintf("get_extmem_conf(): %08x\n", get_extmem_conf());
  size = get_icache_size();
  prefix(size,buf);
  vgaprintf("get_icache_size(): %s\n", buf);
  vgaprintf("get_icache_conf(): %08x\n", get_icache_conf());
  size = get_dcache_size();
  prefix(size,buf);
  vgaprintf("get_dcache_size(): %s\n", buf);
  vgaprintf("get_dcache_conf(): %08x\n", get_dcache_conf());
  size = get_scache_size();
  prefix(size,buf);
  vgaprintf("get_scache_size(): %s\n", buf);
  vgaprintf("get_scache_conf(): %08x\n", get_scache_conf());
  size = get_ispm_size();
  prefix(size,buf);
  vgaprintf("get_ispm_size(): %s\n", buf);
  size = get_dspm_size();
  prefix(size,buf);
  vgaprintf("get_dspm_size(): %s\n", buf);
}


void chess(color A, color B) {
  for (int i = 0; i < VGA_DISPLAY_WIDTH; i++) {
    for (int j = 0; j < VGA_DISPLAY_HEIGHT; j++) {
      if ((i / 100 + j / 100) % 2 == 0) {
        writePixel(i, j, A);
      }
      else {
        writePixel(i, j, B);
      }
    }
  }
}