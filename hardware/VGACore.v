module LineMemory(
  input         clock,
  input  [10:0] io_rdAddr,
  output [15:0] io_rdData,
  input  [15:0] io_wrData,
  input  [10:0] io_wrAddr
);
  reg [15:0] mem [0:1599]; // @[PixelBuffer.scala 20:24]
  reg [31:0] _RAND_0;
  wire [15:0] mem__T_data; // @[PixelBuffer.scala 20:24]
  wire [10:0] mem__T_addr; // @[PixelBuffer.scala 20:24]
  reg [31:0] _RAND_1;
  wire [15:0] mem__T_1_data; // @[PixelBuffer.scala 20:24]
  wire [10:0] mem__T_1_addr; // @[PixelBuffer.scala 20:24]
  wire  mem__T_1_mask; // @[PixelBuffer.scala 20:24]
  wire  mem__T_1_en; // @[PixelBuffer.scala 20:24]
  wire  _GEN_2; // @[PixelBuffer.scala 23:18]
  reg [10:0] mem__T_addr_pipe_0;
  reg [31:0] _RAND_2;
  assign mem__T_addr = mem__T_addr_pipe_0;
  `ifndef RANDOMIZE_GARBAGE_ASSIGN
  assign mem__T_data = mem[mem__T_addr]; // @[PixelBuffer.scala 20:24]
  `else
  assign mem__T_data = mem__T_addr >= 11'h640 ? _RAND_1[15:0] : mem[mem__T_addr]; // @[PixelBuffer.scala 20:24]
  `endif // RANDOMIZE_GARBAGE_ASSIGN
  assign mem__T_1_data = io_wrData;
  assign mem__T_1_addr = io_wrAddr;
  assign mem__T_1_mask = 1'h1;
  assign mem__T_1_en = 1'h1;
  assign _GEN_2 = 1'h1; // @[PixelBuffer.scala 23:18]
  assign io_rdData = mem__T_data; // @[PixelBuffer.scala 21:13]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
  _RAND_0 = {1{`RANDOM}};
  `ifdef RANDOMIZE_MEM_INIT
  for (initvar = 0; initvar < 1600; initvar = initvar+1)
    mem[initvar] = _RAND_0[15:0];
  `endif // RANDOMIZE_MEM_INIT
  _RAND_1 = {1{`RANDOM}};
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  mem__T_addr_pipe_0 = _RAND_2[10:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end
  always @(posedge clock) begin
    if(mem__T_1_en & mem__T_1_mask) begin
      mem[mem__T_1_addr] <= mem__T_1_data; // @[PixelBuffer.scala 20:24]
    end
    if (_GEN_2) begin
      mem__T_addr_pipe_0 <= io_rdAddr;
    end
  end
endmodule
module PixelBuffer(
  input         clock,
  input         reset,
  input         io_enable,
  output [7:0]  io_R,
  output [7:0]  io_G,
  output [7:0]  io_B,
  input  [9:0]  io_h_pos,
  input  [9:0]  io_v_pos,
  input         io_mem_valid,
  input  [31:0] io_mem_data
);
  wire  memory_clock; // @[PixelBuffer.scala 46:22]
  wire [10:0] memory_io_rdAddr; // @[PixelBuffer.scala 46:22]
  wire [15:0] memory_io_rdData; // @[PixelBuffer.scala 46:22]
  wire [15:0] memory_io_wrData; // @[PixelBuffer.scala 46:22]
  wire [10:0] memory_io_wrAddr; // @[PixelBuffer.scala 46:22]
  reg [31:0] recvBuf; // @[PixelBuffer.scala 48:24]
  reg [31:0] _RAND_0;
  reg [8:0] lineAddress; // @[PixelBuffer.scala 50:28]
  reg [31:0] _RAND_1;
  reg [9:0] v_pos_next; // @[PixelBuffer.scala 52:27]
  reg [31:0] _RAND_2;
  wire [9:0] _T_1; // @[PixelBuffer.scala 53:26]
  wire [8:0] _T_3; // @[PixelBuffer.scala 54:30]
  wire  _T_5; // @[PixelBuffer.scala 68:47]
  wire  _T_6; // @[PixelBuffer.scala 68:32]
  wire  _T_9; // @[PixelBuffer.scala 73:17]
  wire  _T_10; // @[PixelBuffer.scala 78:18]
  wire  _T_11; // @[PixelBuffer.scala 78:22]
  wire [8:0] _T_13; // @[PixelBuffer.scala 80:33]
  wire [8:0] _GEN_2; // @[PixelBuffer.scala 78:31]
  wire  _T_14; // @[PixelBuffer.scala 87:16]
  wire  _T_15; // @[PixelBuffer.scala 87:20]
  wire [9:0] _GEN_3; // @[PixelBuffer.scala 88:29]
  wire [10:0] _T_18; // @[Cat.scala 29:58]
  wire [10:0] _T_20; // @[PixelBuffer.scala 95:51]
  wire [10:0] _GEN_4; // @[PixelBuffer.scala 94:29]
  wire [4:0] _T_22; // @[PixelBuffer.scala 106:19]
  wire [7:0] _T_23; // @[PixelBuffer.scala 106:28]
  wire [4:0] _T_24; // @[PixelBuffer.scala 107:19]
  wire [7:0] _T_25; // @[PixelBuffer.scala 107:26]
  wire [4:0] _T_26; // @[PixelBuffer.scala 108:19]
  wire [7:0] _T_27; // @[PixelBuffer.scala 108:26]
  LineMemory memory ( // @[PixelBuffer.scala 46:22]
    .clock(memory_clock),
    .io_rdAddr(memory_io_rdAddr),
    .io_rdData(memory_io_rdData),
    .io_wrData(memory_io_wrData),
    .io_wrAddr(memory_io_wrAddr)
  );
  assign _T_1 = io_v_pos + 10'h1; // @[PixelBuffer.scala 53:26]
  assign _T_3 = lineAddress + 9'h1; // @[PixelBuffer.scala 54:30]
  assign _T_5 = lineAddress < 9'h190; // @[PixelBuffer.scala 68:47]
  assign _T_6 = io_mem_valid & _T_5; // @[PixelBuffer.scala 68:32]
  assign _T_9 = io_h_pos == 10'h0; // @[PixelBuffer.scala 73:17]
  assign _T_10 = v_pos_next[0]; // @[PixelBuffer.scala 78:18]
  assign _T_11 = _T_10 == 1'h0; // @[PixelBuffer.scala 78:22]
  assign _T_13 = 9'h190 + lineAddress; // @[PixelBuffer.scala 80:33]
  assign _GEN_2 = _T_11 ? _T_13 : lineAddress; // @[PixelBuffer.scala 78:31]
  assign _T_14 = io_v_pos[0]; // @[PixelBuffer.scala 87:16]
  assign _T_15 = _T_14 == 1'h0; // @[PixelBuffer.scala 87:20]
  assign _GEN_3 = io_enable ? io_h_pos : 10'h320; // @[PixelBuffer.scala 88:29]
  assign _T_18 = {1'h0,io_h_pos}; // @[Cat.scala 29:58]
  assign _T_20 = _T_18 + 11'h320; // @[PixelBuffer.scala 95:51]
  assign _GEN_4 = io_enable ? _T_20 : 11'h0; // @[PixelBuffer.scala 94:29]
  assign _T_22 = memory_io_rdData[14:10]; // @[PixelBuffer.scala 106:19]
  assign _T_23 = {_T_22, 3'h0}; // @[PixelBuffer.scala 106:28]
  assign _T_24 = memory_io_rdData[9:5]; // @[PixelBuffer.scala 107:19]
  assign _T_25 = {_T_24, 3'h0}; // @[PixelBuffer.scala 107:26]
  assign _T_26 = memory_io_rdData[4:0]; // @[PixelBuffer.scala 108:19]
  assign _T_27 = {_T_26, 3'h0}; // @[PixelBuffer.scala 108:26]
  assign io_R = io_enable ? _T_23 : 8'h0; // @[PixelBuffer.scala 102:8 PixelBuffer.scala 106:10]
  assign io_G = io_enable ? _T_25 : 8'h0; // @[PixelBuffer.scala 103:8 PixelBuffer.scala 107:10]
  assign io_B = io_enable ? _T_27 : 8'h0; // @[PixelBuffer.scala 104:8 PixelBuffer.scala 108:10]
  assign memory_clock = clock;
  assign memory_io_rdAddr = _T_15 ? {{1'd0}, _GEN_3} : _GEN_4; // @[PixelBuffer.scala 89:24 PixelBuffer.scala 91:24 PixelBuffer.scala 95:24 PixelBuffer.scala 97:24]
  assign memory_io_wrData = recvBuf[15:0]; // @[PixelBuffer.scala 65:20]
  assign memory_io_wrAddr = {{2'd0}, _GEN_2}; // @[PixelBuffer.scala 64:20 PixelBuffer.scala 80:24 PixelBuffer.scala 83:24]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  recvBuf = _RAND_0[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  lineAddress = _RAND_1[8:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  v_pos_next = _RAND_2[9:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end
  always @(posedge clock) begin
    if (reset) begin
      recvBuf <= 32'h0;
    end else begin
      recvBuf <= io_mem_data;
    end
    if (reset) begin
      lineAddress <= 9'h0;
    end else begin
      if (_T_9) begin
        lineAddress <= 9'h0;
      end else begin
        if (_T_6) begin
          lineAddress <= _T_3;
        end else begin
          lineAddress <= _T_3;
        end
      end
    end
    if (reset) begin
      v_pos_next <= 10'h0;
    end else begin
      v_pos_next <= _T_1;
    end
  end
endmodule
module VGAController(
  input         clock,
  input         reset,
  output        io_n_blank,
  output        io_h_sync,
  output        io_v_sync,
  output        io_pixel_clock,
  output [7:0]  io_R,
  output [7:0]  io_G,
  output [7:0]  io_B,
  input         io_mem_valid,
  input  [31:0] io_mem_data
);
  wire  PixelBuffer_clock; // @[VGAController.scala 61:27]
  wire  PixelBuffer_reset; // @[VGAController.scala 61:27]
  wire  PixelBuffer_io_enable; // @[VGAController.scala 61:27]
  wire [7:0] PixelBuffer_io_R; // @[VGAController.scala 61:27]
  wire [7:0] PixelBuffer_io_G; // @[VGAController.scala 61:27]
  wire [7:0] PixelBuffer_io_B; // @[VGAController.scala 61:27]
  wire [9:0] PixelBuffer_io_h_pos; // @[VGAController.scala 61:27]
  wire [9:0] PixelBuffer_io_v_pos; // @[VGAController.scala 61:27]
  wire  PixelBuffer_io_mem_valid; // @[VGAController.scala 61:27]
  wire [31:0] PixelBuffer_io_mem_data; // @[VGAController.scala 61:27]
  reg  pixel_clock; // @[VGAController.scala 30:28]
  reg [31:0] _RAND_0;
  wire  _T; // @[VGAController.scala 33:18]
  reg [9:0] v_cntReg; // @[VGAController.scala 57:25]
  reg [31:0] _RAND_1;
  reg [10:0] h_cntReg; // @[VGAController.scala 58:25]
  reg [31:0] _RAND_2;
  wire [10:0] _T_3; // @[VGAController.scala 80:26]
  wire  _T_4; // @[VGAController.scala 83:17]
  wire [9:0] _T_6; // @[VGAController.scala 85:26]
  wire  _T_7; // @[VGAController.scala 88:17]
  wire  _T_8; // @[VGAController.scala 94:18]
  wire  _T_9; // @[VGAController.scala 94:63]
  wire  _T_11; // @[VGAController.scala 102:18]
  wire  _T_12; // @[VGAController.scala 102:63]
  wire  _T_14; // @[VGAController.scala 110:18]
  wire  _T_15; // @[VGAController.scala 110:44]
  PixelBuffer PixelBuffer ( // @[VGAController.scala 61:27]
    .clock(PixelBuffer_clock),
    .reset(PixelBuffer_reset),
    .io_enable(PixelBuffer_io_enable),
    .io_R(PixelBuffer_io_R),
    .io_G(PixelBuffer_io_G),
    .io_B(PixelBuffer_io_B),
    .io_h_pos(PixelBuffer_io_h_pos),
    .io_v_pos(PixelBuffer_io_v_pos),
    .io_mem_valid(PixelBuffer_io_mem_valid),
    .io_mem_data(PixelBuffer_io_mem_data)
  );
  assign _T = ~ pixel_clock; // @[VGAController.scala 33:18]
  assign _T_3 = h_cntReg + 11'h1; // @[VGAController.scala 80:26]
  assign _T_4 = h_cntReg == 11'h41f; // @[VGAController.scala 83:17]
  assign _T_6 = v_cntReg + 10'h1; // @[VGAController.scala 85:26]
  assign _T_7 = v_cntReg == 10'h274; // @[VGAController.scala 88:17]
  assign _T_8 = v_cntReg >= 10'h259; // @[VGAController.scala 94:18]
  assign _T_9 = v_cntReg < 10'h25d; // @[VGAController.scala 94:63]
  assign _T_11 = h_cntReg >= 11'h348; // @[VGAController.scala 102:18]
  assign _T_12 = h_cntReg < 11'h3c8; // @[VGAController.scala 102:63]
  assign _T_14 = h_cntReg < 11'h320; // @[VGAController.scala 110:18]
  assign _T_15 = v_cntReg < 10'h258; // @[VGAController.scala 110:44]
  assign io_n_blank = _T_14 & _T_15; // @[VGAController.scala 36:14 VGAController.scala 111:16 VGAController.scala 114:16]
  assign io_h_sync = _T_11 & _T_12; // @[VGAController.scala 37:13 VGAController.scala 103:15 VGAController.scala 106:15]
  assign io_v_sync = _T_8 & _T_9; // @[VGAController.scala 38:13 VGAController.scala 95:15 VGAController.scala 98:15]
  assign io_pixel_clock = pixel_clock; // @[VGAController.scala 34:18]
  assign io_R = PixelBuffer_io_R; // @[VGAController.scala 74:8]
  assign io_G = PixelBuffer_io_G; // @[VGAController.scala 75:8]
  assign io_B = PixelBuffer_io_B; // @[VGAController.scala 76:8]
  assign PixelBuffer_clock = clock;
  assign PixelBuffer_reset = reset;
  assign PixelBuffer_io_enable = io_n_blank; // @[VGAController.scala 64:25]
  assign PixelBuffer_io_h_pos = h_cntReg[9:0]; // @[VGAController.scala 66:24]
  assign PixelBuffer_io_v_pos = v_cntReg; // @[VGAController.scala 67:24]
  assign PixelBuffer_io_mem_valid = io_mem_valid; // @[VGAController.scala 71:28]
  assign PixelBuffer_io_mem_data = io_mem_data; // @[VGAController.scala 72:27]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  pixel_clock = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  v_cntReg = _RAND_1[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  h_cntReg = _RAND_2[10:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end
  always @(posedge clock) begin
    if (reset) begin
      pixel_clock <= 1'h0;
    end else begin
      pixel_clock <= _T;
    end
    if (reset) begin
      v_cntReg <= 10'h0;
    end else begin
      if (_T_7) begin
        v_cntReg <= 10'h0;
      end else begin
        if (_T_4) begin
          v_cntReg <= _T_6;
        end
      end
    end
    if (reset) begin
      h_cntReg <= 11'h0;
    end else begin
      if (_T_4) begin
        h_cntReg <= 11'h0;
      end else begin
        if (_T) begin
          h_cntReg <= _T_3;
        end
      end
    end
  end
endmodule
module VGACore(
  input         clock,
  input         reset,
  output        io_pixel_clock,
  output        io_n_sync,
  output        io_n_blank,
  output        io_h_sync,
  output        io_v_sync,
  output [7:0]  io_R,
  output [7:0]  io_G,
  output [7:0]  io_B,
  output [2:0]  io_memPort_M_Cmd,
  output [31:0] io_memPort_M_Addr,
  output [31:0] io_memPort_M_Data,
  output        io_memPort_M_DataValid,
  output [3:0]  io_memPort_M_DataByteEn,
  input  [1:0]  io_memPort_S_Resp,
  input  [31:0] io_memPort_S_Data,
  input         io_memPort_S_CmdAccept,
  input         io_memPort_S_DataAccept
);
  wire  controller_clock; // @[VGACore.scala 39:26]
  wire  controller_reset; // @[VGACore.scala 39:26]
  wire  controller_io_n_blank; // @[VGACore.scala 39:26]
  wire  controller_io_h_sync; // @[VGACore.scala 39:26]
  wire  controller_io_v_sync; // @[VGACore.scala 39:26]
  wire  controller_io_pixel_clock; // @[VGACore.scala 39:26]
  wire [7:0] controller_io_R; // @[VGACore.scala 39:26]
  wire [7:0] controller_io_G; // @[VGACore.scala 39:26]
  wire [7:0] controller_io_B; // @[VGACore.scala 39:26]
  wire  controller_io_mem_valid; // @[VGACore.scala 39:26]
  wire [31:0] controller_io_mem_data; // @[VGACore.scala 39:26]
  VGAController controller ( // @[VGACore.scala 39:26]
    .clock(controller_clock),
    .reset(controller_reset),
    .io_n_blank(controller_io_n_blank),
    .io_h_sync(controller_io_h_sync),
    .io_v_sync(controller_io_v_sync),
    .io_pixel_clock(controller_io_pixel_clock),
    .io_R(controller_io_R),
    .io_G(controller_io_G),
    .io_B(controller_io_B),
    .io_mem_valid(controller_io_mem_valid),
    .io_mem_data(controller_io_mem_data)
  );
  assign io_pixel_clock = controller_io_pixel_clock; // @[VGACore.scala 41:18]
  assign io_n_sync = 1'h0; // @[VGACore.scala 37:13]
  assign io_n_blank = controller_io_n_blank; // @[VGACore.scala 42:14]
  assign io_h_sync = controller_io_h_sync; // @[VGACore.scala 43:13]
  assign io_v_sync = controller_io_v_sync; // @[VGACore.scala 44:13]
  assign io_R = controller_io_R; // @[VGACore.scala 46:8]
  assign io_G = controller_io_G; // @[VGACore.scala 47:8]
  assign io_B = controller_io_B; // @[VGACore.scala 48:8]
  assign io_memPort_M_Cmd = 3'h2; // @[VGACore.scala 60:20 VGACore.scala 62:22]
  assign io_memPort_M_Addr = 32'h0; // @[VGACore.scala 58:21]
  assign io_memPort_M_Data = 32'h0; // @[VGACore.scala 51:21]
  assign io_memPort_M_DataValid = 1'h0; // @[VGACore.scala 52:26]
  assign io_memPort_M_DataByteEn = 4'h1; // @[VGACore.scala 57:27]
  assign controller_clock = clock;
  assign controller_reset = reset;
  assign controller_io_mem_valid = io_memPort_S_DataAccept; // @[VGACore.scala 55:27]
  assign controller_io_mem_data = io_memPort_S_Data; // @[VGACore.scala 54:26]
endmodule
