
#include <stdio.h>

#include "vga.h"

#include <machine/patmos.h>
#include <machine/exceptions.h>
#include <machine/rtc.h>
#include <machine/boot.h>

void textAnimation();
void text();
void chess();
void cpuinfo();

int main() {
  fill(black);

  cpuinfo();

  return 0;
}

void textAnimation() {
  renderText(10, 60, white,
  "root@patmos:~# \n");

  renderText(10, 60, white,
  "               _\n");
}

void text() {
  renderText(10, 10, white, "Patmos says: HELLO WORLD!");

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