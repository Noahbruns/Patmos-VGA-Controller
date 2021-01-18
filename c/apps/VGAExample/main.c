
#include <stdio.h>

#include "vga.h"

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

  runPacman(20, 20, 600, 400);

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
  for (int i = 0; i < 10; i++) {
    for (int j = 0; j < 10; j++) {
      writePixel(X + i, Y + j, red);
    }
  }

  int dirX = toX > X ? 2 : -2;

  while(X != toX) {
    for (int i = 0; i < 10; i++) {
      writePixel(X, Y + i, dirX > 0 ? black : red);
      writePixel(X + 1, Y + i, dirX > 0 ? black : red);
    }
    for (int i = 0; i < 10; i++) {
      writePixel(X + 10, Y + i, dirX > 0 ? red : black);
      writePixel(X + 11, Y + i, dirX > 0 ? red : black);
    }
    delay(10);
    X += dirX;
  }

  int dirY = toY > Y ? 2 : -2;

  while(Y != toY) {
    for (int i = 0; i < 10; i++) {
      writePixel(X + i, Y, dirY > 0 ? black : red);
      writePixel(X + i, Y + 1, dirY > 0 ? black : red);
    }
    for (int i = 0; i < 10; i++) {
      writePixel(X + i, Y + 10, dirY > 0 ? red : black);
      writePixel(X + i, Y + 11, dirY > 0 ? black : red);
    }
    delay(10);
    Y += dirY;
  }
}

void pacman() {
  fill(black);

  int x = 20;
  int y = 300;

  int stepX = 1;
  int stepY = 0;

  for (int i = 0; i < 600; i++) {
    renderPacman(x, y, 0, red);
    delay(10);
    renderPacman(x, y, 0, black);
    x += stepX;
    y += stepY;
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