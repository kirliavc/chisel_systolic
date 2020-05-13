module Addition(
  input  [3:0] io_row,
  input  [3:0] io_column,
  input        io_sgn,
  output [7:0] io_out
);
  wire  _T; // @[bitfusion8.scala 18:36]
  wire  row_extend_sgn; // @[bitfusion8.scala 18:28]
  wire  _T_2; // @[bitfusion8.scala 19:39]
  wire  col_extend_sgn; // @[bitfusion8.scala 19:28]
  wire [7:0] _T_5; // @[bitfusion8.scala 22:51]
  wire [7:0] row_offset; // @[bitfusion8.scala 22:20]
  wire [7:0] _T_8; // @[bitfusion8.scala 23:54]
  wire [7:0] col_offset; // @[bitfusion8.scala 23:20]
  wire [7:0] _T_11; // @[bitfusion8.scala 24:26]
  wire [7:0] _T_12; // @[bitfusion8.scala 24:13]
  assign _T = io_row[3]; // @[bitfusion8.scala 18:36]
  assign row_extend_sgn = io_sgn & _T; // @[bitfusion8.scala 18:28]
  assign _T_2 = io_column[3]; // @[bitfusion8.scala 19:39]
  assign col_extend_sgn = io_sgn & _T_2; // @[bitfusion8.scala 19:28]
  assign _T_5 = {io_row, 4'h0}; // @[bitfusion8.scala 22:51]
  assign row_offset = col_extend_sgn ? _T_5 : 8'h0; // @[bitfusion8.scala 22:20]
  assign _T_8 = {io_column, 4'h0}; // @[bitfusion8.scala 23:54]
  assign col_offset = row_extend_sgn ? _T_8 : 8'h0; // @[bitfusion8.scala 23:20]
  assign _T_11 = row_offset + col_offset; // @[bitfusion8.scala 24:26]
  assign _T_12 = ~ _T_11; // @[bitfusion8.scala 24:13]
  assign io_out = _T_12 + 8'h1; // @[bitfusion8.scala 24:10]
endmodule
module Unit4(
  input        clock,
  input        reset,
  input  [3:0] io_in_row,
  input  [3:0] io_in_column,
  input        io_sgn,
  output [3:0] io_out_row,
  output [3:0] io_out_column,
  output [7:0] io_out_p,
  input  [7:0] io_res_in,
  output [7:0] io_res_out
);
  wire [3:0] Addition_io_row; // @[bitfusion8.scala 28:19]
  wire [3:0] Addition_io_column; // @[bitfusion8.scala 28:19]
  wire  Addition_io_sgn; // @[bitfusion8.scala 28:19]
  wire [7:0] Addition_io_out; // @[bitfusion8.scala 28:19]
  reg [3:0] row; // @[bitfusion8.scala 51:20]
  reg [31:0] _RAND_0;
  reg [3:0] column; // @[bitfusion8.scala 52:23]
  reg [31:0] _RAND_1;
  wire [7:0] p_total; // @[bitfusion8.scala 59:17]
  wire [7:0] _T_2; // @[bitfusion8.scala 60:27]
  wire [7:0] addition; // @[bitfusion8.scala 56:22 bitfusion8.scala 57:12]
  Addition Addition ( // @[bitfusion8.scala 28:19]
    .io_row(Addition_io_row),
    .io_column(Addition_io_column),
    .io_sgn(Addition_io_sgn),
    .io_out(Addition_io_out)
  );
  assign p_total = row * column; // @[bitfusion8.scala 59:17]
  assign _T_2 = io_res_in + p_total; // @[bitfusion8.scala 60:27]
  assign addition = Addition_io_out; // @[bitfusion8.scala 56:22 bitfusion8.scala 57:12]
  assign io_out_row = row; // @[bitfusion8.scala 62:14]
  assign io_out_column = column; // @[bitfusion8.scala 63:17]
  assign io_out_p = row * column; // @[bitfusion8.scala 61:12]
  assign io_res_out = _T_2 + addition; // @[bitfusion8.scala 60:14]
  assign Addition_io_row = row; // @[bitfusion8.scala 29:14]
  assign Addition_io_column = column; // @[bitfusion8.scala 30:17]
  assign Addition_io_sgn = io_sgn; // @[bitfusion8.scala 31:14]
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
`ifndef SYNTHESIS
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
  row = _RAND_0[3:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  column = _RAND_1[3:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      row <= 4'h0;
    end else begin
      row <= io_in_row;
    end
    if (reset) begin
      column <= 4'h0;
    end else begin
      column <= io_in_column;
    end
  end
endmodule
module Addition_16(
  input  [3:0]  io_row,
  input  [7:0]  io_column,
  input         io_sgn,
  output [11:0] io_out
);
  wire  _T; // @[bitfusion8.scala 18:36]
  wire  row_extend_sgn; // @[bitfusion8.scala 18:28]
  wire  _T_2; // @[bitfusion8.scala 19:39]
  wire  col_extend_sgn; // @[bitfusion8.scala 19:28]
  wire [11:0] _T_5; // @[bitfusion8.scala 22:51]
  wire [11:0] row_offset; // @[bitfusion8.scala 22:20]
  wire [11:0] _T_8; // @[bitfusion8.scala 23:54]
  wire [11:0] col_offset; // @[bitfusion8.scala 23:20]
  wire [11:0] _T_11; // @[bitfusion8.scala 24:26]
  wire [11:0] _T_12; // @[bitfusion8.scala 24:13]
  assign _T = io_row[3]; // @[bitfusion8.scala 18:36]
  assign row_extend_sgn = io_sgn & _T; // @[bitfusion8.scala 18:28]
  assign _T_2 = io_column[7]; // @[bitfusion8.scala 19:39]
  assign col_extend_sgn = io_sgn & _T_2; // @[bitfusion8.scala 19:28]
  assign _T_5 = {io_row, 8'h0}; // @[bitfusion8.scala 22:51]
  assign row_offset = col_extend_sgn ? _T_5 : 12'h0; // @[bitfusion8.scala 22:20]
  assign _T_8 = {io_column, 4'h0}; // @[bitfusion8.scala 23:54]
  assign col_offset = row_extend_sgn ? _T_8 : 12'h0; // @[bitfusion8.scala 23:20]
  assign _T_11 = row_offset + col_offset; // @[bitfusion8.scala 24:26]
  assign _T_12 = ~ _T_11; // @[bitfusion8.scala 24:13]
  assign io_out = _T_12 + 12'h1; // @[bitfusion8.scala 24:10]
endmodule
module Bridge(
  input         clock,
  input         reset,
  input  [7:0]  io_pl,
  input  [7:0]  io_pr,
  input  [3:0]  io_in_row,
  input  [7:0]  io_in_column,
  input         io_sgn,
  output [3:0]  io_out_row,
  output [7:0]  io_out_column,
  output [11:0] io_out_p,
  input  [15:0] io_res_in,
  output [15:0] io_res_out
);
  wire [3:0] Addition_io_row; // @[bitfusion8.scala 28:19]
  wire [7:0] Addition_io_column; // @[bitfusion8.scala 28:19]
  wire  Addition_io_sgn; // @[bitfusion8.scala 28:19]
  wire [11:0] Addition_io_out; // @[bitfusion8.scala 28:19]
  reg [7:0] pl; // @[bitfusion8.scala 87:19]
  reg [31:0] _RAND_0;
  reg [7:0] pr; // @[bitfusion8.scala 88:19]
  reg [31:0] _RAND_1;
  reg [3:0] row; // @[bitfusion8.scala 89:20]
  reg [31:0] _RAND_2;
  reg [7:0] column; // @[bitfusion8.scala 90:23]
  reg [31:0] _RAND_3;
  wire [11:0] _T; // @[bitfusion8.scala 99:17]
  wire [11:0] _GEN_0; // @[bitfusion8.scala 99:28]
  wire [11:0] p_total; // @[bitfusion8.scala 99:28]
  wire [11:0] addition; // @[bitfusion8.scala 95:22 bitfusion8.scala 96:12]
  wire [11:0] sum; // @[bitfusion8.scala 102:18]
  wire  _T_6; // @[bitfusion8.scala 107:49]
  wire  _T_7; // @[bitfusion8.scala 107:64]
  wire [3:0] _T_9; // @[Bitwise.scala 71:12]
  wire [15:0] _T_10; // @[Cat.scala 29:58]
  Addition_16 Addition ( // @[bitfusion8.scala 28:19]
    .io_row(Addition_io_row),
    .io_column(Addition_io_column),
    .io_sgn(Addition_io_sgn),
    .io_out(Addition_io_out)
  );
  assign _T = {pl, 4'h0}; // @[bitfusion8.scala 99:17]
  assign _GEN_0 = {{4'd0}, pr}; // @[bitfusion8.scala 99:28]
  assign p_total = _T + _GEN_0; // @[bitfusion8.scala 99:28]
  assign addition = Addition_io_out; // @[bitfusion8.scala 95:22 bitfusion8.scala 96:12]
  assign sum = p_total + addition; // @[bitfusion8.scala 102:18]
  assign _T_6 = sum[11]; // @[bitfusion8.scala 107:49]
  assign _T_7 = _T_6 & io_sgn; // @[bitfusion8.scala 107:64]
  assign _T_9 = _T_7 ? 4'hf : 4'h0; // @[Bitwise.scala 71:12]
  assign _T_10 = {_T_9,sum}; // @[Cat.scala 29:58]
  assign io_out_row = row; // @[bitfusion8.scala 111:14]
  assign io_out_column = column; // @[bitfusion8.scala 112:17]
  assign io_out_p = _T + _GEN_0; // @[bitfusion8.scala 110:12]
  assign io_res_out = io_res_in + _T_10; // @[bitfusion8.scala 108:16]
  assign Addition_io_row = row; // @[bitfusion8.scala 29:14]
  assign Addition_io_column = column; // @[bitfusion8.scala 30:17]
  assign Addition_io_sgn = io_sgn; // @[bitfusion8.scala 31:14]
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
`ifndef SYNTHESIS
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
  pl = _RAND_0[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  pr = _RAND_1[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  row = _RAND_2[3:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  column = _RAND_3[7:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      pl <= 8'h0;
    end else begin
      pl <= io_pl;
    end
    if (reset) begin
      pr <= 8'h0;
    end else begin
      pr <= io_pr;
    end
    if (reset) begin
      row <= 4'h0;
    end else begin
      row <= io_in_row;
    end
    if (reset) begin
      column <= 8'h0;
    end else begin
      column <= io_in_column;
    end
  end
endmodule
module Addition_24(
  input  [7:0]  io_row,
  input  [7:0]  io_column,
  input         io_sgn,
  output [15:0] io_out
);
  wire  _T; // @[bitfusion8.scala 18:36]
  wire  row_extend_sgn; // @[bitfusion8.scala 18:28]
  wire  _T_2; // @[bitfusion8.scala 19:39]
  wire  col_extend_sgn; // @[bitfusion8.scala 19:28]
  wire [15:0] _T_5; // @[bitfusion8.scala 22:51]
  wire [15:0] row_offset; // @[bitfusion8.scala 22:20]
  wire [15:0] _T_8; // @[bitfusion8.scala 23:54]
  wire [15:0] col_offset; // @[bitfusion8.scala 23:20]
  wire [15:0] _T_11; // @[bitfusion8.scala 24:26]
  wire [15:0] _T_12; // @[bitfusion8.scala 24:13]
  assign _T = io_row[7]; // @[bitfusion8.scala 18:36]
  assign row_extend_sgn = io_sgn & _T; // @[bitfusion8.scala 18:28]
  assign _T_2 = io_column[7]; // @[bitfusion8.scala 19:39]
  assign col_extend_sgn = io_sgn & _T_2; // @[bitfusion8.scala 19:28]
  assign _T_5 = {io_row, 8'h0}; // @[bitfusion8.scala 22:51]
  assign row_offset = col_extend_sgn ? _T_5 : 16'h0; // @[bitfusion8.scala 22:20]
  assign _T_8 = {io_column, 8'h0}; // @[bitfusion8.scala 23:54]
  assign col_offset = row_extend_sgn ? _T_8 : 16'h0; // @[bitfusion8.scala 23:20]
  assign _T_11 = row_offset + col_offset; // @[bitfusion8.scala 24:26]
  assign _T_12 = ~ _T_11; // @[bitfusion8.scala 24:13]
  assign io_out = _T_12 + 16'h1; // @[bitfusion8.scala 24:10]
endmodule
module Bridge_overflow(
  input         clock,
  input         reset,
  input  [11:0] io_pl,
  input  [11:0] io_pr,
  input  [7:0]  io_in_row,
  input  [7:0]  io_in_column,
  input         io_sgn,
  output [7:0]  io_out_row,
  output [7:0]  io_out_column,
  output [15:0] io_out_p,
  input  [31:0] io_res_in,
  output [31:0] io_res_out
);
  wire [7:0] Addition_io_row; // @[bitfusion8.scala 28:19]
  wire [7:0] Addition_io_column; // @[bitfusion8.scala 28:19]
  wire  Addition_io_sgn; // @[bitfusion8.scala 28:19]
  wire [15:0] Addition_io_out; // @[bitfusion8.scala 28:19]
  reg [11:0] pl; // @[bitfusion8.scala 139:19]
  reg [31:0] _RAND_0;
  reg [11:0] pr; // @[bitfusion8.scala 140:19]
  reg [31:0] _RAND_1;
  reg [7:0] row; // @[bitfusion8.scala 141:20]
  reg [31:0] _RAND_2;
  reg [7:0] column; // @[bitfusion8.scala 142:23]
  reg [31:0] _RAND_3;
  wire [15:0] _T; // @[bitfusion8.scala 151:17]
  wire [15:0] _GEN_5; // @[bitfusion8.scala 151:28]
  wire [15:0] p_total; // @[bitfusion8.scala 151:28]
  wire [15:0] addition; // @[bitfusion8.scala 147:22 bitfusion8.scala 148:12]
  wire [15:0] sum; // @[bitfusion8.scala 156:18]
  wire  _T_5; // @[bitfusion8.scala 158:15]
  wire  _T_6; // @[bitfusion8.scala 159:14]
  wire [15:0] _GEN_0; // @[bitfusion8.scala 159:22]
  wire  _T_7; // @[bitfusion8.scala 165:13]
  wire  _T_9; // @[bitfusion8.scala 165:18]
  wire  _T_10; // @[bitfusion8.scala 166:16]
  wire [15:0] _GEN_1; // @[bitfusion8.scala 166:24]
  wire  _T_11; // @[bitfusion8.scala 173:16]
  wire [15:0] _GEN_2; // @[bitfusion8.scala 173:26]
  wire [15:0] _GEN_3; // @[bitfusion8.scala 165:30]
  wire [15:0] sum_od; // @[bitfusion8.scala 158:23]
  wire  _T_13; // @[bitfusion8.scala 188:52]
  wire  _T_14; // @[bitfusion8.scala 188:67]
  wire [15:0] _T_16; // @[Bitwise.scala 71:12]
  wire [31:0] _T_17; // @[Cat.scala 29:58]
  Addition_24 Addition ( // @[bitfusion8.scala 28:19]
    .io_row(Addition_io_row),
    .io_column(Addition_io_column),
    .io_sgn(Addition_io_sgn),
    .io_out(Addition_io_out)
  );
  assign _T = {pl, 4'h0}; // @[bitfusion8.scala 151:17]
  assign _GEN_5 = {{4'd0}, pr}; // @[bitfusion8.scala 151:28]
  assign p_total = _T + _GEN_5; // @[bitfusion8.scala 151:28]
  assign addition = Addition_io_out; // @[bitfusion8.scala 147:22 bitfusion8.scala 148:12]
  assign sum = p_total + addition; // @[bitfusion8.scala 156:18]
  assign _T_5 = io_sgn == 1'h0; // @[bitfusion8.scala 158:15]
  assign _T_6 = sum > 16'hff; // @[bitfusion8.scala 159:14]
  assign _GEN_0 = _T_6 ? 16'hff : sum; // @[bitfusion8.scala 159:22]
  assign _T_7 = sum[15]; // @[bitfusion8.scala 165:13]
  assign _T_9 = _T_7 ^ io_sgn; // @[bitfusion8.scala 165:18]
  assign _T_10 = sum > 16'h7f; // @[bitfusion8.scala 166:16]
  assign _GEN_1 = _T_10 ? 16'h7f : sum; // @[bitfusion8.scala 166:24]
  assign _T_11 = sum < 16'hff80; // @[bitfusion8.scala 173:16]
  assign _GEN_2 = _T_11 ? 16'hff80 : sum; // @[bitfusion8.scala 173:26]
  assign _GEN_3 = _T_9 ? _GEN_1 : _GEN_2; // @[bitfusion8.scala 165:30]
  assign sum_od = _T_5 ? _GEN_0 : _GEN_3; // @[bitfusion8.scala 158:23]
  assign _T_13 = sum_od[15]; // @[bitfusion8.scala 188:52]
  assign _T_14 = _T_13 & io_sgn; // @[bitfusion8.scala 188:67]
  assign _T_16 = _T_14 ? 16'hffff : 16'h0; // @[Bitwise.scala 71:12]
  assign _T_17 = {_T_16,sum_od}; // @[Cat.scala 29:58]
  assign io_out_row = row; // @[bitfusion8.scala 192:14]
  assign io_out_column = column; // @[bitfusion8.scala 193:17]
  assign io_out_p = _T + _GEN_5; // @[bitfusion8.scala 191:12]
  assign io_res_out = io_res_in + _T_17; // @[bitfusion8.scala 189:16]
  assign Addition_io_row = row; // @[bitfusion8.scala 29:14]
  assign Addition_io_column = column; // @[bitfusion8.scala 30:17]
  assign Addition_io_sgn = io_sgn; // @[bitfusion8.scala 31:14]
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
`ifndef SYNTHESIS
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
  pl = _RAND_0[11:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  pr = _RAND_1[11:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  row = _RAND_2[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  column = _RAND_3[7:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      pl <= 12'h0;
    end else begin
      pl <= io_pl;
    end
    if (reset) begin
      pr <= 12'h0;
    end else begin
      pr <= io_pr;
    end
    if (reset) begin
      row <= 8'h0;
    end else begin
      row <= io_in_row;
    end
    if (reset) begin
      column <= 8'h0;
    end else begin
      column <= io_in_column;
    end
  end
endmodule
module Addition_28(
  input  [7:0]  io_row,
  input  [15:0] io_column,
  input         io_sgn,
  output [23:0] io_out
);
  wire  _T; // @[bitfusion8.scala 18:36]
  wire  row_extend_sgn; // @[bitfusion8.scala 18:28]
  wire  _T_2; // @[bitfusion8.scala 19:39]
  wire  col_extend_sgn; // @[bitfusion8.scala 19:28]
  wire [23:0] _T_5; // @[bitfusion8.scala 22:51]
  wire [23:0] row_offset; // @[bitfusion8.scala 22:20]
  wire [23:0] _T_8; // @[bitfusion8.scala 23:54]
  wire [23:0] col_offset; // @[bitfusion8.scala 23:20]
  wire [23:0] _T_11; // @[bitfusion8.scala 24:26]
  wire [23:0] _T_12; // @[bitfusion8.scala 24:13]
  assign _T = io_row[7]; // @[bitfusion8.scala 18:36]
  assign row_extend_sgn = io_sgn & _T; // @[bitfusion8.scala 18:28]
  assign _T_2 = io_column[15]; // @[bitfusion8.scala 19:39]
  assign col_extend_sgn = io_sgn & _T_2; // @[bitfusion8.scala 19:28]
  assign _T_5 = {io_row, 16'h0}; // @[bitfusion8.scala 22:51]
  assign row_offset = col_extend_sgn ? _T_5 : 24'h0; // @[bitfusion8.scala 22:20]
  assign _T_8 = {io_column, 8'h0}; // @[bitfusion8.scala 23:54]
  assign col_offset = row_extend_sgn ? _T_8 : 24'h0; // @[bitfusion8.scala 23:20]
  assign _T_11 = row_offset + col_offset; // @[bitfusion8.scala 24:26]
  assign _T_12 = ~ _T_11; // @[bitfusion8.scala 24:13]
  assign io_out = _T_12 + 24'h1; // @[bitfusion8.scala 24:10]
endmodule
module Bridge_8(
  input         clock,
  input         reset,
  input  [15:0] io_pl,
  input  [15:0] io_pr,
  input  [7:0]  io_in_row,
  input  [15:0] io_in_column,
  input         io_sgn,
  output [7:0]  io_out_row,
  output [15:0] io_out_column,
  output [23:0] io_out_p,
  input  [31:0] io_res_in,
  output [31:0] io_res_out
);
  wire [7:0] Addition_io_row; // @[bitfusion8.scala 28:19]
  wire [15:0] Addition_io_column; // @[bitfusion8.scala 28:19]
  wire  Addition_io_sgn; // @[bitfusion8.scala 28:19]
  wire [23:0] Addition_io_out; // @[bitfusion8.scala 28:19]
  reg [15:0] pl; // @[bitfusion8.scala 87:19]
  reg [31:0] _RAND_0;
  reg [15:0] pr; // @[bitfusion8.scala 88:19]
  reg [31:0] _RAND_1;
  reg [7:0] row; // @[bitfusion8.scala 89:20]
  reg [31:0] _RAND_2;
  reg [15:0] column; // @[bitfusion8.scala 90:23]
  reg [31:0] _RAND_3;
  wire [23:0] _T; // @[bitfusion8.scala 99:17]
  wire [23:0] _GEN_0; // @[bitfusion8.scala 99:28]
  wire [23:0] p_total; // @[bitfusion8.scala 99:28]
  wire [23:0] addition; // @[bitfusion8.scala 95:22 bitfusion8.scala 96:12]
  wire [23:0] sum; // @[bitfusion8.scala 102:18]
  wire  _T_6; // @[bitfusion8.scala 107:49]
  wire  _T_7; // @[bitfusion8.scala 107:64]
  wire [7:0] _T_9; // @[Bitwise.scala 71:12]
  wire [31:0] _T_10; // @[Cat.scala 29:58]
  Addition_28 Addition ( // @[bitfusion8.scala 28:19]
    .io_row(Addition_io_row),
    .io_column(Addition_io_column),
    .io_sgn(Addition_io_sgn),
    .io_out(Addition_io_out)
  );
  assign _T = {pl, 8'h0}; // @[bitfusion8.scala 99:17]
  assign _GEN_0 = {{8'd0}, pr}; // @[bitfusion8.scala 99:28]
  assign p_total = _T + _GEN_0; // @[bitfusion8.scala 99:28]
  assign addition = Addition_io_out; // @[bitfusion8.scala 95:22 bitfusion8.scala 96:12]
  assign sum = p_total + addition; // @[bitfusion8.scala 102:18]
  assign _T_6 = sum[23]; // @[bitfusion8.scala 107:49]
  assign _T_7 = _T_6 & io_sgn; // @[bitfusion8.scala 107:64]
  assign _T_9 = _T_7 ? 8'hff : 8'h0; // @[Bitwise.scala 71:12]
  assign _T_10 = {_T_9,sum}; // @[Cat.scala 29:58]
  assign io_out_row = row; // @[bitfusion8.scala 111:14]
  assign io_out_column = column; // @[bitfusion8.scala 112:17]
  assign io_out_p = _T + _GEN_0; // @[bitfusion8.scala 110:12]
  assign io_res_out = io_res_in + _T_10; // @[bitfusion8.scala 108:16]
  assign Addition_io_row = row; // @[bitfusion8.scala 29:14]
  assign Addition_io_column = column; // @[bitfusion8.scala 30:17]
  assign Addition_io_sgn = io_sgn; // @[bitfusion8.scala 31:14]
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
`ifndef SYNTHESIS
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
  pl = _RAND_0[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  pr = _RAND_1[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  row = _RAND_2[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  column = _RAND_3[15:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      pl <= 16'h0;
    end else begin
      pl <= io_pl;
    end
    if (reset) begin
      pr <= 16'h0;
    end else begin
      pr <= io_pr;
    end
    if (reset) begin
      row <= 8'h0;
    end else begin
      row <= io_in_row;
    end
    if (reset) begin
      column <= 16'h0;
    end else begin
      column <= io_in_column;
    end
  end
endmodule
module Addition_30(
  input  [15:0] io_row,
  input  [15:0] io_column,
  input         io_sgn,
  output [31:0] io_out
);
  wire  _T; // @[bitfusion8.scala 18:36]
  wire  row_extend_sgn; // @[bitfusion8.scala 18:28]
  wire  _T_2; // @[bitfusion8.scala 19:39]
  wire  col_extend_sgn; // @[bitfusion8.scala 19:28]
  wire [31:0] _T_5; // @[bitfusion8.scala 22:51]
  wire [31:0] row_offset; // @[bitfusion8.scala 22:20]
  wire [31:0] _T_8; // @[bitfusion8.scala 23:54]
  wire [31:0] col_offset; // @[bitfusion8.scala 23:20]
  wire [31:0] _T_11; // @[bitfusion8.scala 24:26]
  wire [31:0] _T_12; // @[bitfusion8.scala 24:13]
  assign _T = io_row[15]; // @[bitfusion8.scala 18:36]
  assign row_extend_sgn = io_sgn & _T; // @[bitfusion8.scala 18:28]
  assign _T_2 = io_column[15]; // @[bitfusion8.scala 19:39]
  assign col_extend_sgn = io_sgn & _T_2; // @[bitfusion8.scala 19:28]
  assign _T_5 = {io_row, 16'h0}; // @[bitfusion8.scala 22:51]
  assign row_offset = col_extend_sgn ? _T_5 : 32'h0; // @[bitfusion8.scala 22:20]
  assign _T_8 = {io_column, 16'h0}; // @[bitfusion8.scala 23:54]
  assign col_offset = row_extend_sgn ? _T_8 : 32'h0; // @[bitfusion8.scala 23:20]
  assign _T_11 = row_offset + col_offset; // @[bitfusion8.scala 24:26]
  assign _T_12 = ~ _T_11; // @[bitfusion8.scala 24:13]
  assign io_out = _T_12 + 32'h1; // @[bitfusion8.scala 24:10]
endmodule
module Bridge_10(
  input         clock,
  input         reset,
  input  [23:0] io_pl,
  input  [23:0] io_pr,
  input  [15:0] io_in_row,
  input  [15:0] io_in_column,
  input         io_sgn,
  input  [31:0] io_res_in,
  output [31:0] io_res_out
);
  wire [15:0] Addition_io_row; // @[bitfusion8.scala 28:19]
  wire [15:0] Addition_io_column; // @[bitfusion8.scala 28:19]
  wire  Addition_io_sgn; // @[bitfusion8.scala 28:19]
  wire [31:0] Addition_io_out; // @[bitfusion8.scala 28:19]
  reg [23:0] pl; // @[bitfusion8.scala 87:19]
  reg [31:0] _RAND_0;
  reg [23:0] pr; // @[bitfusion8.scala 88:19]
  reg [31:0] _RAND_1;
  reg [15:0] row; // @[bitfusion8.scala 89:20]
  reg [31:0] _RAND_2;
  reg [15:0] column; // @[bitfusion8.scala 90:23]
  reg [31:0] _RAND_3;
  wire [31:0] _T; // @[bitfusion8.scala 99:17]
  wire [31:0] _GEN_0; // @[bitfusion8.scala 99:28]
  wire [31:0] p_total; // @[bitfusion8.scala 99:28]
  wire [31:0] addition; // @[bitfusion8.scala 95:22 bitfusion8.scala 96:12]
  wire [31:0] sum; // @[bitfusion8.scala 102:18]
  Addition_30 Addition ( // @[bitfusion8.scala 28:19]
    .io_row(Addition_io_row),
    .io_column(Addition_io_column),
    .io_sgn(Addition_io_sgn),
    .io_out(Addition_io_out)
  );
  assign _T = {pl, 8'h0}; // @[bitfusion8.scala 99:17]
  assign _GEN_0 = {{8'd0}, pr}; // @[bitfusion8.scala 99:28]
  assign p_total = _T + _GEN_0; // @[bitfusion8.scala 99:28]
  assign addition = Addition_io_out; // @[bitfusion8.scala 95:22 bitfusion8.scala 96:12]
  assign sum = p_total + addition; // @[bitfusion8.scala 102:18]
  assign io_res_out = io_res_in + sum; // @[bitfusion8.scala 104:16]
  assign Addition_io_row = row; // @[bitfusion8.scala 29:14]
  assign Addition_io_column = column; // @[bitfusion8.scala 30:17]
  assign Addition_io_sgn = io_sgn; // @[bitfusion8.scala 31:14]
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
`ifndef SYNTHESIS
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
  pl = _RAND_0[23:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  pr = _RAND_1[23:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  row = _RAND_2[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  column = _RAND_3[15:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      pl <= 24'h0;
    end else begin
      pl <= io_pl;
    end
    if (reset) begin
      pr <= 24'h0;
    end else begin
      pr <= io_pr;
    end
    if (reset) begin
      row <= 16'h0;
    end else begin
      row <= io_in_row;
    end
    if (reset) begin
      column <= 16'h0;
    end else begin
      column <= io_in_column;
    end
  end
endmodule
module DynamicPE_WS(
  input          clock,
  input          reset,
  input  [2:0]   io_ctrl,
  input  [15:0]  io_in_row,
  input  [15:0]  io_in_column,
  input          io_sgn,
  input  [127:0] io_statC_in,
  output [127:0] io_statC_out
);
  wire  Unit4_0_0_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_0_0_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_0_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_0_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_0_0_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_0_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_0_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_0_0_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_0_0_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_0_0_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_0_1_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_0_1_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_1_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_1_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_0_1_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_1_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_1_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_0_1_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_0_1_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_0_1_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_0_2_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_0_2_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_2_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_2_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_0_2_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_2_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_2_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_0_2_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_0_2_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_0_2_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_0_3_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_0_3_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_3_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_3_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_0_3_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_3_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_0_3_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_0_3_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_0_3_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_0_3_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_1_0_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_1_0_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_0_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_0_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_1_0_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_0_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_0_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_1_0_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_1_0_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_1_0_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_1_1_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_1_1_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_1_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_1_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_1_1_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_1_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_1_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_1_1_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_1_1_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_1_1_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_1_2_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_1_2_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_2_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_2_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_1_2_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_2_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_2_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_1_2_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_1_2_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_1_2_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_1_3_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_1_3_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_3_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_3_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_1_3_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_3_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_1_3_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_1_3_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_1_3_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_1_3_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_2_0_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_2_0_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_0_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_0_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_2_0_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_0_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_0_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_2_0_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_2_0_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_2_0_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_2_1_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_2_1_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_1_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_1_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_2_1_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_1_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_1_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_2_1_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_2_1_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_2_1_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_2_2_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_2_2_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_2_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_2_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_2_2_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_2_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_2_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_2_2_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_2_2_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_2_2_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_2_3_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_2_3_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_3_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_3_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_2_3_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_3_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_2_3_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_2_3_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_2_3_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_2_3_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_3_0_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_3_0_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_0_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_0_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_3_0_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_0_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_0_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_3_0_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_3_0_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_3_0_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_3_1_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_3_1_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_1_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_1_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_3_1_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_1_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_1_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_3_1_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_3_1_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_3_1_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_3_2_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_3_2_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_2_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_2_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_3_2_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_2_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_2_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_3_2_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_3_2_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_3_2_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Unit4_3_3_clock; // @[bitfusion8.scala 208:13]
  wire  Unit4_3_3_reset; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_3_io_in_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_3_io_in_column; // @[bitfusion8.scala 208:13]
  wire  Unit4_3_3_io_sgn; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_3_io_out_row; // @[bitfusion8.scala 208:13]
  wire [3:0] Unit4_3_3_io_out_column; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_3_3_io_out_p; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_3_3_io_res_in; // @[bitfusion8.scala 208:13]
  wire [7:0] Unit4_3_3_io_res_out; // @[bitfusion8.scala 208:13]
  wire  Bridge2_0_0_clock; // @[bitfusion8.scala 220:13]
  wire  Bridge2_0_0_reset; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_0_0_io_pl; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_0_0_io_pr; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_0_0_io_in_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_0_0_io_in_column; // @[bitfusion8.scala 220:13]
  wire  Bridge2_0_0_io_sgn; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_0_0_io_out_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_0_0_io_out_column; // @[bitfusion8.scala 220:13]
  wire [11:0] Bridge2_0_0_io_out_p; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_0_0_io_res_in; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_0_0_io_res_out; // @[bitfusion8.scala 220:13]
  wire  Bridge2_0_1_clock; // @[bitfusion8.scala 220:13]
  wire  Bridge2_0_1_reset; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_0_1_io_pl; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_0_1_io_pr; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_0_1_io_in_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_0_1_io_in_column; // @[bitfusion8.scala 220:13]
  wire  Bridge2_0_1_io_sgn; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_0_1_io_out_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_0_1_io_out_column; // @[bitfusion8.scala 220:13]
  wire [11:0] Bridge2_0_1_io_out_p; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_0_1_io_res_in; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_0_1_io_res_out; // @[bitfusion8.scala 220:13]
  wire  Bridge2_1_0_clock; // @[bitfusion8.scala 220:13]
  wire  Bridge2_1_0_reset; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_1_0_io_pl; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_1_0_io_pr; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_1_0_io_in_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_1_0_io_in_column; // @[bitfusion8.scala 220:13]
  wire  Bridge2_1_0_io_sgn; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_1_0_io_out_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_1_0_io_out_column; // @[bitfusion8.scala 220:13]
  wire [11:0] Bridge2_1_0_io_out_p; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_1_0_io_res_in; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_1_0_io_res_out; // @[bitfusion8.scala 220:13]
  wire  Bridge2_1_1_clock; // @[bitfusion8.scala 220:13]
  wire  Bridge2_1_1_reset; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_1_1_io_pl; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_1_1_io_pr; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_1_1_io_in_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_1_1_io_in_column; // @[bitfusion8.scala 220:13]
  wire  Bridge2_1_1_io_sgn; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_1_1_io_out_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_1_1_io_out_column; // @[bitfusion8.scala 220:13]
  wire [11:0] Bridge2_1_1_io_out_p; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_1_1_io_res_in; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_1_1_io_res_out; // @[bitfusion8.scala 220:13]
  wire  Bridge2_2_0_clock; // @[bitfusion8.scala 220:13]
  wire  Bridge2_2_0_reset; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_2_0_io_pl; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_2_0_io_pr; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_2_0_io_in_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_2_0_io_in_column; // @[bitfusion8.scala 220:13]
  wire  Bridge2_2_0_io_sgn; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_2_0_io_out_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_2_0_io_out_column; // @[bitfusion8.scala 220:13]
  wire [11:0] Bridge2_2_0_io_out_p; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_2_0_io_res_in; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_2_0_io_res_out; // @[bitfusion8.scala 220:13]
  wire  Bridge2_2_1_clock; // @[bitfusion8.scala 220:13]
  wire  Bridge2_2_1_reset; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_2_1_io_pl; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_2_1_io_pr; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_2_1_io_in_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_2_1_io_in_column; // @[bitfusion8.scala 220:13]
  wire  Bridge2_2_1_io_sgn; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_2_1_io_out_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_2_1_io_out_column; // @[bitfusion8.scala 220:13]
  wire [11:0] Bridge2_2_1_io_out_p; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_2_1_io_res_in; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_2_1_io_res_out; // @[bitfusion8.scala 220:13]
  wire  Bridge2_3_0_clock; // @[bitfusion8.scala 220:13]
  wire  Bridge2_3_0_reset; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_3_0_io_pl; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_3_0_io_pr; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_3_0_io_in_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_3_0_io_in_column; // @[bitfusion8.scala 220:13]
  wire  Bridge2_3_0_io_sgn; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_3_0_io_out_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_3_0_io_out_column; // @[bitfusion8.scala 220:13]
  wire [11:0] Bridge2_3_0_io_out_p; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_3_0_io_res_in; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_3_0_io_res_out; // @[bitfusion8.scala 220:13]
  wire  Bridge2_3_1_clock; // @[bitfusion8.scala 220:13]
  wire  Bridge2_3_1_reset; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_3_1_io_pl; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_3_1_io_pr; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_3_1_io_in_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_3_1_io_in_column; // @[bitfusion8.scala 220:13]
  wire  Bridge2_3_1_io_sgn; // @[bitfusion8.scala 220:13]
  wire [3:0] Bridge2_3_1_io_out_row; // @[bitfusion8.scala 220:13]
  wire [7:0] Bridge2_3_1_io_out_column; // @[bitfusion8.scala 220:13]
  wire [11:0] Bridge2_3_1_io_out_p; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_3_1_io_res_in; // @[bitfusion8.scala 220:13]
  wire [15:0] Bridge2_3_1_io_res_out; // @[bitfusion8.scala 220:13]
  wire  Bridge4_col_0_0_clock; // @[bitfusion8.scala 237:13]
  wire  Bridge4_col_0_0_reset; // @[bitfusion8.scala 237:13]
  wire [11:0] Bridge4_col_0_0_io_pl; // @[bitfusion8.scala 237:13]
  wire [11:0] Bridge4_col_0_0_io_pr; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_0_0_io_in_row; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_0_0_io_in_column; // @[bitfusion8.scala 237:13]
  wire  Bridge4_col_0_0_io_sgn; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_0_0_io_out_row; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_0_0_io_out_column; // @[bitfusion8.scala 237:13]
  wire [15:0] Bridge4_col_0_0_io_out_p; // @[bitfusion8.scala 237:13]
  wire [31:0] Bridge4_col_0_0_io_res_in; // @[bitfusion8.scala 237:13]
  wire [31:0] Bridge4_col_0_0_io_res_out; // @[bitfusion8.scala 237:13]
  wire  Bridge4_col_0_1_clock; // @[bitfusion8.scala 237:13]
  wire  Bridge4_col_0_1_reset; // @[bitfusion8.scala 237:13]
  wire [11:0] Bridge4_col_0_1_io_pl; // @[bitfusion8.scala 237:13]
  wire [11:0] Bridge4_col_0_1_io_pr; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_0_1_io_in_row; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_0_1_io_in_column; // @[bitfusion8.scala 237:13]
  wire  Bridge4_col_0_1_io_sgn; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_0_1_io_out_row; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_0_1_io_out_column; // @[bitfusion8.scala 237:13]
  wire [15:0] Bridge4_col_0_1_io_out_p; // @[bitfusion8.scala 237:13]
  wire [31:0] Bridge4_col_0_1_io_res_in; // @[bitfusion8.scala 237:13]
  wire [31:0] Bridge4_col_0_1_io_res_out; // @[bitfusion8.scala 237:13]
  wire  Bridge4_col_1_0_clock; // @[bitfusion8.scala 237:13]
  wire  Bridge4_col_1_0_reset; // @[bitfusion8.scala 237:13]
  wire [11:0] Bridge4_col_1_0_io_pl; // @[bitfusion8.scala 237:13]
  wire [11:0] Bridge4_col_1_0_io_pr; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_1_0_io_in_row; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_1_0_io_in_column; // @[bitfusion8.scala 237:13]
  wire  Bridge4_col_1_0_io_sgn; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_1_0_io_out_row; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_1_0_io_out_column; // @[bitfusion8.scala 237:13]
  wire [15:0] Bridge4_col_1_0_io_out_p; // @[bitfusion8.scala 237:13]
  wire [31:0] Bridge4_col_1_0_io_res_in; // @[bitfusion8.scala 237:13]
  wire [31:0] Bridge4_col_1_0_io_res_out; // @[bitfusion8.scala 237:13]
  wire  Bridge4_col_1_1_clock; // @[bitfusion8.scala 237:13]
  wire  Bridge4_col_1_1_reset; // @[bitfusion8.scala 237:13]
  wire [11:0] Bridge4_col_1_1_io_pl; // @[bitfusion8.scala 237:13]
  wire [11:0] Bridge4_col_1_1_io_pr; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_1_1_io_in_row; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_1_1_io_in_column; // @[bitfusion8.scala 237:13]
  wire  Bridge4_col_1_1_io_sgn; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_1_1_io_out_row; // @[bitfusion8.scala 237:13]
  wire [7:0] Bridge4_col_1_1_io_out_column; // @[bitfusion8.scala 237:13]
  wire [15:0] Bridge4_col_1_1_io_out_p; // @[bitfusion8.scala 237:13]
  wire [31:0] Bridge4_col_1_1_io_res_in; // @[bitfusion8.scala 237:13]
  wire [31:0] Bridge4_col_1_1_io_res_out; // @[bitfusion8.scala 237:13]
  wire  Bridge8_0_clock; // @[bitfusion8.scala 251:11]
  wire  Bridge8_0_reset; // @[bitfusion8.scala 251:11]
  wire [15:0] Bridge8_0_io_pl; // @[bitfusion8.scala 251:11]
  wire [15:0] Bridge8_0_io_pr; // @[bitfusion8.scala 251:11]
  wire [7:0] Bridge8_0_io_in_row; // @[bitfusion8.scala 251:11]
  wire [15:0] Bridge8_0_io_in_column; // @[bitfusion8.scala 251:11]
  wire  Bridge8_0_io_sgn; // @[bitfusion8.scala 251:11]
  wire [7:0] Bridge8_0_io_out_row; // @[bitfusion8.scala 251:11]
  wire [15:0] Bridge8_0_io_out_column; // @[bitfusion8.scala 251:11]
  wire [23:0] Bridge8_0_io_out_p; // @[bitfusion8.scala 251:11]
  wire [31:0] Bridge8_0_io_res_in; // @[bitfusion8.scala 251:11]
  wire [31:0] Bridge8_0_io_res_out; // @[bitfusion8.scala 251:11]
  wire  Bridge8_1_clock; // @[bitfusion8.scala 251:11]
  wire  Bridge8_1_reset; // @[bitfusion8.scala 251:11]
  wire [15:0] Bridge8_1_io_pl; // @[bitfusion8.scala 251:11]
  wire [15:0] Bridge8_1_io_pr; // @[bitfusion8.scala 251:11]
  wire [7:0] Bridge8_1_io_in_row; // @[bitfusion8.scala 251:11]
  wire [15:0] Bridge8_1_io_in_column; // @[bitfusion8.scala 251:11]
  wire  Bridge8_1_io_sgn; // @[bitfusion8.scala 251:11]
  wire [7:0] Bridge8_1_io_out_row; // @[bitfusion8.scala 251:11]
  wire [15:0] Bridge8_1_io_out_column; // @[bitfusion8.scala 251:11]
  wire [23:0] Bridge8_1_io_out_p; // @[bitfusion8.scala 251:11]
  wire [31:0] Bridge8_1_io_res_in; // @[bitfusion8.scala 251:11]
  wire [31:0] Bridge8_1_io_res_out; // @[bitfusion8.scala 251:11]
  wire  Bridge16_clock; // @[bitfusion8.scala 262:24]
  wire  Bridge16_reset; // @[bitfusion8.scala 262:24]
  wire [23:0] Bridge16_io_pl; // @[bitfusion8.scala 262:24]
  wire [23:0] Bridge16_io_pr; // @[bitfusion8.scala 262:24]
  wire [15:0] Bridge16_io_in_row; // @[bitfusion8.scala 262:24]
  wire [15:0] Bridge16_io_in_column; // @[bitfusion8.scala 262:24]
  wire  Bridge16_io_sgn; // @[bitfusion8.scala 262:24]
  wire [31:0] Bridge16_io_res_in; // @[bitfusion8.scala 262:24]
  wire [31:0] Bridge16_io_res_out; // @[bitfusion8.scala 262:24]
  wire [7:0] res404_out_1; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_0; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_3; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_2; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_5; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_4; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_7; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_6; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [63:0] _T_84; // @[bitfusion8.scala 319:29]
  wire [7:0] res404_out_9; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_8; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_11; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_10; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_13; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_12; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_15; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [7:0] res404_out_14; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  wire [127:0] _T_92; // @[bitfusion8.scala 319:29]
  wire [15:0] res408_out_1; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  wire [15:0] res408_out_0; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  wire [15:0] res408_out_3; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  wire [15:0] res408_out_2; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  wire [15:0] res408_out_5; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  wire [15:0] res408_out_4; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  wire [15:0] res408_out_7; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  wire [15:0] res408_out_6; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  wire [127:0] _T_99; // @[bitfusion8.scala 319:55]
  wire [31:0] res808_out_1; // @[bitfusion8.scala 296:24 bitfusion8.scala 311:25]
  wire [31:0] res808_out_0; // @[bitfusion8.scala 296:24 bitfusion8.scala 311:25]
  wire [31:0] res808_out_3; // @[bitfusion8.scala 296:24 bitfusion8.scala 311:25]
  wire [31:0] res808_out_2; // @[bitfusion8.scala 296:24 bitfusion8.scala 311:25]
  wire [127:0] _T_102; // @[bitfusion8.scala 319:80]
  wire [31:0] res8016_out_1; // @[bitfusion8.scala 297:25 bitfusion8.scala 314:20]
  wire [31:0] res8016_out_0; // @[bitfusion8.scala 297:25 bitfusion8.scala 314:20]
  wire [63:0] _T_103; // @[bitfusion8.scala 319:107]
  wire  _T_104; // @[Mux.scala 68:19]
  wire [31:0] res16016_out; // @[bitfusion8.scala 298:26 bitfusion8.scala 316:16]
  wire [31:0] _T_105; // @[Mux.scala 68:16]
  wire  _T_106; // @[Mux.scala 68:19]
  wire [63:0] _T_107; // @[Mux.scala 68:16]
  wire  _T_108; // @[Mux.scala 68:19]
  wire [127:0] _T_109; // @[Mux.scala 68:16]
  wire  _T_110; // @[Mux.scala 68:19]
  wire [127:0] _T_111; // @[Mux.scala 68:16]
  wire  _T_112; // @[Mux.scala 68:19]
  Unit4 Unit4_0_0 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_0_0_clock),
    .reset(Unit4_0_0_reset),
    .io_in_row(Unit4_0_0_io_in_row),
    .io_in_column(Unit4_0_0_io_in_column),
    .io_sgn(Unit4_0_0_io_sgn),
    .io_out_row(Unit4_0_0_io_out_row),
    .io_out_column(Unit4_0_0_io_out_column),
    .io_out_p(Unit4_0_0_io_out_p),
    .io_res_in(Unit4_0_0_io_res_in),
    .io_res_out(Unit4_0_0_io_res_out)
  );
  Unit4 Unit4_0_1 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_0_1_clock),
    .reset(Unit4_0_1_reset),
    .io_in_row(Unit4_0_1_io_in_row),
    .io_in_column(Unit4_0_1_io_in_column),
    .io_sgn(Unit4_0_1_io_sgn),
    .io_out_row(Unit4_0_1_io_out_row),
    .io_out_column(Unit4_0_1_io_out_column),
    .io_out_p(Unit4_0_1_io_out_p),
    .io_res_in(Unit4_0_1_io_res_in),
    .io_res_out(Unit4_0_1_io_res_out)
  );
  Unit4 Unit4_0_2 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_0_2_clock),
    .reset(Unit4_0_2_reset),
    .io_in_row(Unit4_0_2_io_in_row),
    .io_in_column(Unit4_0_2_io_in_column),
    .io_sgn(Unit4_0_2_io_sgn),
    .io_out_row(Unit4_0_2_io_out_row),
    .io_out_column(Unit4_0_2_io_out_column),
    .io_out_p(Unit4_0_2_io_out_p),
    .io_res_in(Unit4_0_2_io_res_in),
    .io_res_out(Unit4_0_2_io_res_out)
  );
  Unit4 Unit4_0_3 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_0_3_clock),
    .reset(Unit4_0_3_reset),
    .io_in_row(Unit4_0_3_io_in_row),
    .io_in_column(Unit4_0_3_io_in_column),
    .io_sgn(Unit4_0_3_io_sgn),
    .io_out_row(Unit4_0_3_io_out_row),
    .io_out_column(Unit4_0_3_io_out_column),
    .io_out_p(Unit4_0_3_io_out_p),
    .io_res_in(Unit4_0_3_io_res_in),
    .io_res_out(Unit4_0_3_io_res_out)
  );
  Unit4 Unit4_1_0 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_1_0_clock),
    .reset(Unit4_1_0_reset),
    .io_in_row(Unit4_1_0_io_in_row),
    .io_in_column(Unit4_1_0_io_in_column),
    .io_sgn(Unit4_1_0_io_sgn),
    .io_out_row(Unit4_1_0_io_out_row),
    .io_out_column(Unit4_1_0_io_out_column),
    .io_out_p(Unit4_1_0_io_out_p),
    .io_res_in(Unit4_1_0_io_res_in),
    .io_res_out(Unit4_1_0_io_res_out)
  );
  Unit4 Unit4_1_1 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_1_1_clock),
    .reset(Unit4_1_1_reset),
    .io_in_row(Unit4_1_1_io_in_row),
    .io_in_column(Unit4_1_1_io_in_column),
    .io_sgn(Unit4_1_1_io_sgn),
    .io_out_row(Unit4_1_1_io_out_row),
    .io_out_column(Unit4_1_1_io_out_column),
    .io_out_p(Unit4_1_1_io_out_p),
    .io_res_in(Unit4_1_1_io_res_in),
    .io_res_out(Unit4_1_1_io_res_out)
  );
  Unit4 Unit4_1_2 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_1_2_clock),
    .reset(Unit4_1_2_reset),
    .io_in_row(Unit4_1_2_io_in_row),
    .io_in_column(Unit4_1_2_io_in_column),
    .io_sgn(Unit4_1_2_io_sgn),
    .io_out_row(Unit4_1_2_io_out_row),
    .io_out_column(Unit4_1_2_io_out_column),
    .io_out_p(Unit4_1_2_io_out_p),
    .io_res_in(Unit4_1_2_io_res_in),
    .io_res_out(Unit4_1_2_io_res_out)
  );
  Unit4 Unit4_1_3 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_1_3_clock),
    .reset(Unit4_1_3_reset),
    .io_in_row(Unit4_1_3_io_in_row),
    .io_in_column(Unit4_1_3_io_in_column),
    .io_sgn(Unit4_1_3_io_sgn),
    .io_out_row(Unit4_1_3_io_out_row),
    .io_out_column(Unit4_1_3_io_out_column),
    .io_out_p(Unit4_1_3_io_out_p),
    .io_res_in(Unit4_1_3_io_res_in),
    .io_res_out(Unit4_1_3_io_res_out)
  );
  Unit4 Unit4_2_0 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_2_0_clock),
    .reset(Unit4_2_0_reset),
    .io_in_row(Unit4_2_0_io_in_row),
    .io_in_column(Unit4_2_0_io_in_column),
    .io_sgn(Unit4_2_0_io_sgn),
    .io_out_row(Unit4_2_0_io_out_row),
    .io_out_column(Unit4_2_0_io_out_column),
    .io_out_p(Unit4_2_0_io_out_p),
    .io_res_in(Unit4_2_0_io_res_in),
    .io_res_out(Unit4_2_0_io_res_out)
  );
  Unit4 Unit4_2_1 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_2_1_clock),
    .reset(Unit4_2_1_reset),
    .io_in_row(Unit4_2_1_io_in_row),
    .io_in_column(Unit4_2_1_io_in_column),
    .io_sgn(Unit4_2_1_io_sgn),
    .io_out_row(Unit4_2_1_io_out_row),
    .io_out_column(Unit4_2_1_io_out_column),
    .io_out_p(Unit4_2_1_io_out_p),
    .io_res_in(Unit4_2_1_io_res_in),
    .io_res_out(Unit4_2_1_io_res_out)
  );
  Unit4 Unit4_2_2 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_2_2_clock),
    .reset(Unit4_2_2_reset),
    .io_in_row(Unit4_2_2_io_in_row),
    .io_in_column(Unit4_2_2_io_in_column),
    .io_sgn(Unit4_2_2_io_sgn),
    .io_out_row(Unit4_2_2_io_out_row),
    .io_out_column(Unit4_2_2_io_out_column),
    .io_out_p(Unit4_2_2_io_out_p),
    .io_res_in(Unit4_2_2_io_res_in),
    .io_res_out(Unit4_2_2_io_res_out)
  );
  Unit4 Unit4_2_3 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_2_3_clock),
    .reset(Unit4_2_3_reset),
    .io_in_row(Unit4_2_3_io_in_row),
    .io_in_column(Unit4_2_3_io_in_column),
    .io_sgn(Unit4_2_3_io_sgn),
    .io_out_row(Unit4_2_3_io_out_row),
    .io_out_column(Unit4_2_3_io_out_column),
    .io_out_p(Unit4_2_3_io_out_p),
    .io_res_in(Unit4_2_3_io_res_in),
    .io_res_out(Unit4_2_3_io_res_out)
  );
  Unit4 Unit4_3_0 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_3_0_clock),
    .reset(Unit4_3_0_reset),
    .io_in_row(Unit4_3_0_io_in_row),
    .io_in_column(Unit4_3_0_io_in_column),
    .io_sgn(Unit4_3_0_io_sgn),
    .io_out_row(Unit4_3_0_io_out_row),
    .io_out_column(Unit4_3_0_io_out_column),
    .io_out_p(Unit4_3_0_io_out_p),
    .io_res_in(Unit4_3_0_io_res_in),
    .io_res_out(Unit4_3_0_io_res_out)
  );
  Unit4 Unit4_3_1 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_3_1_clock),
    .reset(Unit4_3_1_reset),
    .io_in_row(Unit4_3_1_io_in_row),
    .io_in_column(Unit4_3_1_io_in_column),
    .io_sgn(Unit4_3_1_io_sgn),
    .io_out_row(Unit4_3_1_io_out_row),
    .io_out_column(Unit4_3_1_io_out_column),
    .io_out_p(Unit4_3_1_io_out_p),
    .io_res_in(Unit4_3_1_io_res_in),
    .io_res_out(Unit4_3_1_io_res_out)
  );
  Unit4 Unit4_3_2 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_3_2_clock),
    .reset(Unit4_3_2_reset),
    .io_in_row(Unit4_3_2_io_in_row),
    .io_in_column(Unit4_3_2_io_in_column),
    .io_sgn(Unit4_3_2_io_sgn),
    .io_out_row(Unit4_3_2_io_out_row),
    .io_out_column(Unit4_3_2_io_out_column),
    .io_out_p(Unit4_3_2_io_out_p),
    .io_res_in(Unit4_3_2_io_res_in),
    .io_res_out(Unit4_3_2_io_res_out)
  );
  Unit4 Unit4_3_3 ( // @[bitfusion8.scala 208:13]
    .clock(Unit4_3_3_clock),
    .reset(Unit4_3_3_reset),
    .io_in_row(Unit4_3_3_io_in_row),
    .io_in_column(Unit4_3_3_io_in_column),
    .io_sgn(Unit4_3_3_io_sgn),
    .io_out_row(Unit4_3_3_io_out_row),
    .io_out_column(Unit4_3_3_io_out_column),
    .io_out_p(Unit4_3_3_io_out_p),
    .io_res_in(Unit4_3_3_io_res_in),
    .io_res_out(Unit4_3_3_io_res_out)
  );
  Bridge Bridge2_0_0 ( // @[bitfusion8.scala 220:13]
    .clock(Bridge2_0_0_clock),
    .reset(Bridge2_0_0_reset),
    .io_pl(Bridge2_0_0_io_pl),
    .io_pr(Bridge2_0_0_io_pr),
    .io_in_row(Bridge2_0_0_io_in_row),
    .io_in_column(Bridge2_0_0_io_in_column),
    .io_sgn(Bridge2_0_0_io_sgn),
    .io_out_row(Bridge2_0_0_io_out_row),
    .io_out_column(Bridge2_0_0_io_out_column),
    .io_out_p(Bridge2_0_0_io_out_p),
    .io_res_in(Bridge2_0_0_io_res_in),
    .io_res_out(Bridge2_0_0_io_res_out)
  );
  Bridge Bridge2_0_1 ( // @[bitfusion8.scala 220:13]
    .clock(Bridge2_0_1_clock),
    .reset(Bridge2_0_1_reset),
    .io_pl(Bridge2_0_1_io_pl),
    .io_pr(Bridge2_0_1_io_pr),
    .io_in_row(Bridge2_0_1_io_in_row),
    .io_in_column(Bridge2_0_1_io_in_column),
    .io_sgn(Bridge2_0_1_io_sgn),
    .io_out_row(Bridge2_0_1_io_out_row),
    .io_out_column(Bridge2_0_1_io_out_column),
    .io_out_p(Bridge2_0_1_io_out_p),
    .io_res_in(Bridge2_0_1_io_res_in),
    .io_res_out(Bridge2_0_1_io_res_out)
  );
  Bridge Bridge2_1_0 ( // @[bitfusion8.scala 220:13]
    .clock(Bridge2_1_0_clock),
    .reset(Bridge2_1_0_reset),
    .io_pl(Bridge2_1_0_io_pl),
    .io_pr(Bridge2_1_0_io_pr),
    .io_in_row(Bridge2_1_0_io_in_row),
    .io_in_column(Bridge2_1_0_io_in_column),
    .io_sgn(Bridge2_1_0_io_sgn),
    .io_out_row(Bridge2_1_0_io_out_row),
    .io_out_column(Bridge2_1_0_io_out_column),
    .io_out_p(Bridge2_1_0_io_out_p),
    .io_res_in(Bridge2_1_0_io_res_in),
    .io_res_out(Bridge2_1_0_io_res_out)
  );
  Bridge Bridge2_1_1 ( // @[bitfusion8.scala 220:13]
    .clock(Bridge2_1_1_clock),
    .reset(Bridge2_1_1_reset),
    .io_pl(Bridge2_1_1_io_pl),
    .io_pr(Bridge2_1_1_io_pr),
    .io_in_row(Bridge2_1_1_io_in_row),
    .io_in_column(Bridge2_1_1_io_in_column),
    .io_sgn(Bridge2_1_1_io_sgn),
    .io_out_row(Bridge2_1_1_io_out_row),
    .io_out_column(Bridge2_1_1_io_out_column),
    .io_out_p(Bridge2_1_1_io_out_p),
    .io_res_in(Bridge2_1_1_io_res_in),
    .io_res_out(Bridge2_1_1_io_res_out)
  );
  Bridge Bridge2_2_0 ( // @[bitfusion8.scala 220:13]
    .clock(Bridge2_2_0_clock),
    .reset(Bridge2_2_0_reset),
    .io_pl(Bridge2_2_0_io_pl),
    .io_pr(Bridge2_2_0_io_pr),
    .io_in_row(Bridge2_2_0_io_in_row),
    .io_in_column(Bridge2_2_0_io_in_column),
    .io_sgn(Bridge2_2_0_io_sgn),
    .io_out_row(Bridge2_2_0_io_out_row),
    .io_out_column(Bridge2_2_0_io_out_column),
    .io_out_p(Bridge2_2_0_io_out_p),
    .io_res_in(Bridge2_2_0_io_res_in),
    .io_res_out(Bridge2_2_0_io_res_out)
  );
  Bridge Bridge2_2_1 ( // @[bitfusion8.scala 220:13]
    .clock(Bridge2_2_1_clock),
    .reset(Bridge2_2_1_reset),
    .io_pl(Bridge2_2_1_io_pl),
    .io_pr(Bridge2_2_1_io_pr),
    .io_in_row(Bridge2_2_1_io_in_row),
    .io_in_column(Bridge2_2_1_io_in_column),
    .io_sgn(Bridge2_2_1_io_sgn),
    .io_out_row(Bridge2_2_1_io_out_row),
    .io_out_column(Bridge2_2_1_io_out_column),
    .io_out_p(Bridge2_2_1_io_out_p),
    .io_res_in(Bridge2_2_1_io_res_in),
    .io_res_out(Bridge2_2_1_io_res_out)
  );
  Bridge Bridge2_3_0 ( // @[bitfusion8.scala 220:13]
    .clock(Bridge2_3_0_clock),
    .reset(Bridge2_3_0_reset),
    .io_pl(Bridge2_3_0_io_pl),
    .io_pr(Bridge2_3_0_io_pr),
    .io_in_row(Bridge2_3_0_io_in_row),
    .io_in_column(Bridge2_3_0_io_in_column),
    .io_sgn(Bridge2_3_0_io_sgn),
    .io_out_row(Bridge2_3_0_io_out_row),
    .io_out_column(Bridge2_3_0_io_out_column),
    .io_out_p(Bridge2_3_0_io_out_p),
    .io_res_in(Bridge2_3_0_io_res_in),
    .io_res_out(Bridge2_3_0_io_res_out)
  );
  Bridge Bridge2_3_1 ( // @[bitfusion8.scala 220:13]
    .clock(Bridge2_3_1_clock),
    .reset(Bridge2_3_1_reset),
    .io_pl(Bridge2_3_1_io_pl),
    .io_pr(Bridge2_3_1_io_pr),
    .io_in_row(Bridge2_3_1_io_in_row),
    .io_in_column(Bridge2_3_1_io_in_column),
    .io_sgn(Bridge2_3_1_io_sgn),
    .io_out_row(Bridge2_3_1_io_out_row),
    .io_out_column(Bridge2_3_1_io_out_column),
    .io_out_p(Bridge2_3_1_io_out_p),
    .io_res_in(Bridge2_3_1_io_res_in),
    .io_res_out(Bridge2_3_1_io_res_out)
  );
  Bridge_overflow Bridge4_col_0_0 ( // @[bitfusion8.scala 237:13]
    .clock(Bridge4_col_0_0_clock),
    .reset(Bridge4_col_0_0_reset),
    .io_pl(Bridge4_col_0_0_io_pl),
    .io_pr(Bridge4_col_0_0_io_pr),
    .io_in_row(Bridge4_col_0_0_io_in_row),
    .io_in_column(Bridge4_col_0_0_io_in_column),
    .io_sgn(Bridge4_col_0_0_io_sgn),
    .io_out_row(Bridge4_col_0_0_io_out_row),
    .io_out_column(Bridge4_col_0_0_io_out_column),
    .io_out_p(Bridge4_col_0_0_io_out_p),
    .io_res_in(Bridge4_col_0_0_io_res_in),
    .io_res_out(Bridge4_col_0_0_io_res_out)
  );
  Bridge_overflow Bridge4_col_0_1 ( // @[bitfusion8.scala 237:13]
    .clock(Bridge4_col_0_1_clock),
    .reset(Bridge4_col_0_1_reset),
    .io_pl(Bridge4_col_0_1_io_pl),
    .io_pr(Bridge4_col_0_1_io_pr),
    .io_in_row(Bridge4_col_0_1_io_in_row),
    .io_in_column(Bridge4_col_0_1_io_in_column),
    .io_sgn(Bridge4_col_0_1_io_sgn),
    .io_out_row(Bridge4_col_0_1_io_out_row),
    .io_out_column(Bridge4_col_0_1_io_out_column),
    .io_out_p(Bridge4_col_0_1_io_out_p),
    .io_res_in(Bridge4_col_0_1_io_res_in),
    .io_res_out(Bridge4_col_0_1_io_res_out)
  );
  Bridge_overflow Bridge4_col_1_0 ( // @[bitfusion8.scala 237:13]
    .clock(Bridge4_col_1_0_clock),
    .reset(Bridge4_col_1_0_reset),
    .io_pl(Bridge4_col_1_0_io_pl),
    .io_pr(Bridge4_col_1_0_io_pr),
    .io_in_row(Bridge4_col_1_0_io_in_row),
    .io_in_column(Bridge4_col_1_0_io_in_column),
    .io_sgn(Bridge4_col_1_0_io_sgn),
    .io_out_row(Bridge4_col_1_0_io_out_row),
    .io_out_column(Bridge4_col_1_0_io_out_column),
    .io_out_p(Bridge4_col_1_0_io_out_p),
    .io_res_in(Bridge4_col_1_0_io_res_in),
    .io_res_out(Bridge4_col_1_0_io_res_out)
  );
  Bridge_overflow Bridge4_col_1_1 ( // @[bitfusion8.scala 237:13]
    .clock(Bridge4_col_1_1_clock),
    .reset(Bridge4_col_1_1_reset),
    .io_pl(Bridge4_col_1_1_io_pl),
    .io_pr(Bridge4_col_1_1_io_pr),
    .io_in_row(Bridge4_col_1_1_io_in_row),
    .io_in_column(Bridge4_col_1_1_io_in_column),
    .io_sgn(Bridge4_col_1_1_io_sgn),
    .io_out_row(Bridge4_col_1_1_io_out_row),
    .io_out_column(Bridge4_col_1_1_io_out_column),
    .io_out_p(Bridge4_col_1_1_io_out_p),
    .io_res_in(Bridge4_col_1_1_io_res_in),
    .io_res_out(Bridge4_col_1_1_io_res_out)
  );
  Bridge_8 Bridge8_0 ( // @[bitfusion8.scala 251:11]
    .clock(Bridge8_0_clock),
    .reset(Bridge8_0_reset),
    .io_pl(Bridge8_0_io_pl),
    .io_pr(Bridge8_0_io_pr),
    .io_in_row(Bridge8_0_io_in_row),
    .io_in_column(Bridge8_0_io_in_column),
    .io_sgn(Bridge8_0_io_sgn),
    .io_out_row(Bridge8_0_io_out_row),
    .io_out_column(Bridge8_0_io_out_column),
    .io_out_p(Bridge8_0_io_out_p),
    .io_res_in(Bridge8_0_io_res_in),
    .io_res_out(Bridge8_0_io_res_out)
  );
  Bridge_8 Bridge8_1 ( // @[bitfusion8.scala 251:11]
    .clock(Bridge8_1_clock),
    .reset(Bridge8_1_reset),
    .io_pl(Bridge8_1_io_pl),
    .io_pr(Bridge8_1_io_pr),
    .io_in_row(Bridge8_1_io_in_row),
    .io_in_column(Bridge8_1_io_in_column),
    .io_sgn(Bridge8_1_io_sgn),
    .io_out_row(Bridge8_1_io_out_row),
    .io_out_column(Bridge8_1_io_out_column),
    .io_out_p(Bridge8_1_io_out_p),
    .io_res_in(Bridge8_1_io_res_in),
    .io_res_out(Bridge8_1_io_res_out)
  );
  Bridge_10 Bridge16 ( // @[bitfusion8.scala 262:24]
    .clock(Bridge16_clock),
    .reset(Bridge16_reset),
    .io_pl(Bridge16_io_pl),
    .io_pr(Bridge16_io_pr),
    .io_in_row(Bridge16_io_in_row),
    .io_in_column(Bridge16_io_in_column),
    .io_sgn(Bridge16_io_sgn),
    .io_res_in(Bridge16_io_res_in),
    .io_res_out(Bridge16_io_res_out)
  );
  assign res404_out_1 = Unit4_0_1_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_0 = Unit4_0_0_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_3 = Unit4_0_3_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_2 = Unit4_0_2_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_5 = Unit4_1_1_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_4 = Unit4_1_0_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_7 = Unit4_1_3_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_6 = Unit4_1_2_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign _T_84 = {res404_out_7,res404_out_6,res404_out_5,res404_out_4,res404_out_3,res404_out_2,res404_out_1,res404_out_0}; // @[bitfusion8.scala 319:29]
  assign res404_out_9 = Unit4_2_1_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_8 = Unit4_2_0_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_11 = Unit4_2_3_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_10 = Unit4_2_2_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_13 = Unit4_3_1_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_12 = Unit4_3_0_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_15 = Unit4_3_3_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign res404_out_14 = Unit4_3_2_io_res_out; // @[bitfusion8.scala 294:24 bitfusion8.scala 302:25]
  assign _T_92 = {res404_out_15,res404_out_14,res404_out_13,res404_out_12,res404_out_11,res404_out_10,res404_out_9,res404_out_8,_T_84}; // @[bitfusion8.scala 319:29]
  assign res408_out_1 = Bridge2_0_1_io_res_out; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  assign res408_out_0 = Bridge2_0_0_io_res_out; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  assign res408_out_3 = Bridge2_1_1_io_res_out; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  assign res408_out_2 = Bridge2_1_0_io_res_out; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  assign res408_out_5 = Bridge2_2_1_io_res_out; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  assign res408_out_4 = Bridge2_2_0_io_res_out; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  assign res408_out_7 = Bridge2_3_1_io_res_out; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  assign res408_out_6 = Bridge2_3_0_io_res_out; // @[bitfusion8.scala 295:24 bitfusion8.scala 306:25]
  assign _T_99 = {res408_out_7,res408_out_6,res408_out_5,res408_out_4,res408_out_3,res408_out_2,res408_out_1,res408_out_0}; // @[bitfusion8.scala 319:55]
  assign res808_out_1 = Bridge4_col_0_1_io_res_out; // @[bitfusion8.scala 296:24 bitfusion8.scala 311:25]
  assign res808_out_0 = Bridge4_col_0_0_io_res_out; // @[bitfusion8.scala 296:24 bitfusion8.scala 311:25]
  assign res808_out_3 = Bridge4_col_1_1_io_res_out; // @[bitfusion8.scala 296:24 bitfusion8.scala 311:25]
  assign res808_out_2 = Bridge4_col_1_0_io_res_out; // @[bitfusion8.scala 296:24 bitfusion8.scala 311:25]
  assign _T_102 = {res808_out_3,res808_out_2,res808_out_1,res808_out_0}; // @[bitfusion8.scala 319:80]
  assign res8016_out_1 = Bridge8_1_io_res_out; // @[bitfusion8.scala 297:25 bitfusion8.scala 314:20]
  assign res8016_out_0 = Bridge8_0_io_res_out; // @[bitfusion8.scala 297:25 bitfusion8.scala 314:20]
  assign _T_103 = {res8016_out_1,res8016_out_0}; // @[bitfusion8.scala 319:107]
  assign _T_104 = 3'h6 == io_ctrl; // @[Mux.scala 68:19]
  assign res16016_out = Bridge16_io_res_out; // @[bitfusion8.scala 298:26 bitfusion8.scala 316:16]
  assign _T_105 = _T_104 ? res16016_out : 32'h0; // @[Mux.scala 68:16]
  assign _T_106 = 3'h5 == io_ctrl; // @[Mux.scala 68:19]
  assign _T_107 = _T_106 ? _T_103 : {{32'd0}, _T_105}; // @[Mux.scala 68:16]
  assign _T_108 = 3'h4 == io_ctrl; // @[Mux.scala 68:19]
  assign _T_109 = _T_108 ? _T_102 : {{64'd0}, _T_107}; // @[Mux.scala 68:16]
  assign _T_110 = 3'h2 == io_ctrl; // @[Mux.scala 68:19]
  assign _T_111 = _T_110 ? _T_99 : _T_109; // @[Mux.scala 68:16]
  assign _T_112 = 3'h1 == io_ctrl; // @[Mux.scala 68:19]
  assign io_statC_out = _T_112 ? _T_92 : _T_111; // @[bitfusion8.scala 318:16]
  assign Unit4_0_0_clock = clock;
  assign Unit4_0_0_reset = reset;
  assign Unit4_0_0_io_in_row = io_in_row[15:12]; // @[bitfusion8.scala 47:15]
  assign Unit4_0_0_io_in_column = io_in_column[15:12]; // @[bitfusion8.scala 48:18]
  assign Unit4_0_0_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_0_0_io_res_in = io_statC_in[127:120]; // @[bitfusion8.scala 276:29]
  assign Unit4_0_1_clock = clock;
  assign Unit4_0_1_reset = reset;
  assign Unit4_0_1_io_in_row = io_in_row[15:12]; // @[bitfusion8.scala 47:15]
  assign Unit4_0_1_io_in_column = io_in_column[11:8]; // @[bitfusion8.scala 48:18]
  assign Unit4_0_1_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_0_1_io_res_in = io_statC_in[119:112]; // @[bitfusion8.scala 276:29]
  assign Unit4_0_2_clock = clock;
  assign Unit4_0_2_reset = reset;
  assign Unit4_0_2_io_in_row = io_in_row[15:12]; // @[bitfusion8.scala 47:15]
  assign Unit4_0_2_io_in_column = io_in_column[7:4]; // @[bitfusion8.scala 48:18]
  assign Unit4_0_2_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_0_2_io_res_in = io_statC_in[111:104]; // @[bitfusion8.scala 276:29]
  assign Unit4_0_3_clock = clock;
  assign Unit4_0_3_reset = reset;
  assign Unit4_0_3_io_in_row = io_in_row[15:12]; // @[bitfusion8.scala 47:15]
  assign Unit4_0_3_io_in_column = io_in_column[3:0]; // @[bitfusion8.scala 48:18]
  assign Unit4_0_3_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_0_3_io_res_in = io_statC_in[103:96]; // @[bitfusion8.scala 276:29]
  assign Unit4_1_0_clock = clock;
  assign Unit4_1_0_reset = reset;
  assign Unit4_1_0_io_in_row = io_in_row[11:8]; // @[bitfusion8.scala 47:15]
  assign Unit4_1_0_io_in_column = io_in_column[15:12]; // @[bitfusion8.scala 48:18]
  assign Unit4_1_0_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_1_0_io_res_in = io_statC_in[95:88]; // @[bitfusion8.scala 276:29]
  assign Unit4_1_1_clock = clock;
  assign Unit4_1_1_reset = reset;
  assign Unit4_1_1_io_in_row = io_in_row[11:8]; // @[bitfusion8.scala 47:15]
  assign Unit4_1_1_io_in_column = io_in_column[11:8]; // @[bitfusion8.scala 48:18]
  assign Unit4_1_1_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_1_1_io_res_in = io_statC_in[87:80]; // @[bitfusion8.scala 276:29]
  assign Unit4_1_2_clock = clock;
  assign Unit4_1_2_reset = reset;
  assign Unit4_1_2_io_in_row = io_in_row[11:8]; // @[bitfusion8.scala 47:15]
  assign Unit4_1_2_io_in_column = io_in_column[7:4]; // @[bitfusion8.scala 48:18]
  assign Unit4_1_2_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_1_2_io_res_in = io_statC_in[79:72]; // @[bitfusion8.scala 276:29]
  assign Unit4_1_3_clock = clock;
  assign Unit4_1_3_reset = reset;
  assign Unit4_1_3_io_in_row = io_in_row[11:8]; // @[bitfusion8.scala 47:15]
  assign Unit4_1_3_io_in_column = io_in_column[3:0]; // @[bitfusion8.scala 48:18]
  assign Unit4_1_3_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_1_3_io_res_in = io_statC_in[71:64]; // @[bitfusion8.scala 276:29]
  assign Unit4_2_0_clock = clock;
  assign Unit4_2_0_reset = reset;
  assign Unit4_2_0_io_in_row = io_in_row[7:4]; // @[bitfusion8.scala 47:15]
  assign Unit4_2_0_io_in_column = io_in_column[15:12]; // @[bitfusion8.scala 48:18]
  assign Unit4_2_0_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_2_0_io_res_in = io_statC_in[63:56]; // @[bitfusion8.scala 276:29]
  assign Unit4_2_1_clock = clock;
  assign Unit4_2_1_reset = reset;
  assign Unit4_2_1_io_in_row = io_in_row[7:4]; // @[bitfusion8.scala 47:15]
  assign Unit4_2_1_io_in_column = io_in_column[11:8]; // @[bitfusion8.scala 48:18]
  assign Unit4_2_1_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_2_1_io_res_in = io_statC_in[55:48]; // @[bitfusion8.scala 276:29]
  assign Unit4_2_2_clock = clock;
  assign Unit4_2_2_reset = reset;
  assign Unit4_2_2_io_in_row = io_in_row[7:4]; // @[bitfusion8.scala 47:15]
  assign Unit4_2_2_io_in_column = io_in_column[7:4]; // @[bitfusion8.scala 48:18]
  assign Unit4_2_2_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_2_2_io_res_in = io_statC_in[47:40]; // @[bitfusion8.scala 276:29]
  assign Unit4_2_3_clock = clock;
  assign Unit4_2_3_reset = reset;
  assign Unit4_2_3_io_in_row = io_in_row[7:4]; // @[bitfusion8.scala 47:15]
  assign Unit4_2_3_io_in_column = io_in_column[3:0]; // @[bitfusion8.scala 48:18]
  assign Unit4_2_3_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_2_3_io_res_in = io_statC_in[39:32]; // @[bitfusion8.scala 276:29]
  assign Unit4_3_0_clock = clock;
  assign Unit4_3_0_reset = reset;
  assign Unit4_3_0_io_in_row = io_in_row[3:0]; // @[bitfusion8.scala 47:15]
  assign Unit4_3_0_io_in_column = io_in_column[15:12]; // @[bitfusion8.scala 48:18]
  assign Unit4_3_0_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_3_0_io_res_in = io_statC_in[31:24]; // @[bitfusion8.scala 276:29]
  assign Unit4_3_1_clock = clock;
  assign Unit4_3_1_reset = reset;
  assign Unit4_3_1_io_in_row = io_in_row[3:0]; // @[bitfusion8.scala 47:15]
  assign Unit4_3_1_io_in_column = io_in_column[11:8]; // @[bitfusion8.scala 48:18]
  assign Unit4_3_1_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_3_1_io_res_in = io_statC_in[23:16]; // @[bitfusion8.scala 276:29]
  assign Unit4_3_2_clock = clock;
  assign Unit4_3_2_reset = reset;
  assign Unit4_3_2_io_in_row = io_in_row[3:0]; // @[bitfusion8.scala 47:15]
  assign Unit4_3_2_io_in_column = io_in_column[7:4]; // @[bitfusion8.scala 48:18]
  assign Unit4_3_2_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_3_2_io_res_in = io_statC_in[15:8]; // @[bitfusion8.scala 276:29]
  assign Unit4_3_3_clock = clock;
  assign Unit4_3_3_reset = reset;
  assign Unit4_3_3_io_in_row = io_in_row[3:0]; // @[bitfusion8.scala 47:15]
  assign Unit4_3_3_io_in_column = io_in_column[3:0]; // @[bitfusion8.scala 48:18]
  assign Unit4_3_3_io_sgn = io_sgn; // @[bitfusion8.scala 49:12]
  assign Unit4_3_3_io_res_in = io_statC_in[7:0]; // @[bitfusion8.scala 276:29]
  assign Bridge2_0_0_clock = clock;
  assign Bridge2_0_0_reset = reset;
  assign Bridge2_0_0_io_pl = Unit4_0_0_io_out_p; // @[bitfusion8.scala 81:11]
  assign Bridge2_0_0_io_pr = Unit4_0_1_io_out_p; // @[bitfusion8.scala 82:11]
  assign Bridge2_0_0_io_in_row = Unit4_0_0_io_out_row; // @[bitfusion8.scala 83:15]
  assign Bridge2_0_0_io_in_column = {Unit4_0_0_io_out_column,Unit4_0_1_io_out_column}; // @[bitfusion8.scala 84:18]
  assign Bridge2_0_0_io_sgn = io_sgn; // @[bitfusion8.scala 85:12]
  assign Bridge2_0_0_io_res_in = io_statC_in[127:112]; // @[bitfusion8.scala 281:31]
  assign Bridge2_0_1_clock = clock;
  assign Bridge2_0_1_reset = reset;
  assign Bridge2_0_1_io_pl = Unit4_0_2_io_out_p; // @[bitfusion8.scala 81:11]
  assign Bridge2_0_1_io_pr = Unit4_0_3_io_out_p; // @[bitfusion8.scala 82:11]
  assign Bridge2_0_1_io_in_row = Unit4_0_2_io_out_row; // @[bitfusion8.scala 83:15]
  assign Bridge2_0_1_io_in_column = {Unit4_0_2_io_out_column,Unit4_0_3_io_out_column}; // @[bitfusion8.scala 84:18]
  assign Bridge2_0_1_io_sgn = io_sgn; // @[bitfusion8.scala 85:12]
  assign Bridge2_0_1_io_res_in = io_statC_in[111:96]; // @[bitfusion8.scala 281:31]
  assign Bridge2_1_0_clock = clock;
  assign Bridge2_1_0_reset = reset;
  assign Bridge2_1_0_io_pl = Unit4_1_0_io_out_p; // @[bitfusion8.scala 81:11]
  assign Bridge2_1_0_io_pr = Unit4_1_1_io_out_p; // @[bitfusion8.scala 82:11]
  assign Bridge2_1_0_io_in_row = Unit4_1_0_io_out_row; // @[bitfusion8.scala 83:15]
  assign Bridge2_1_0_io_in_column = {Unit4_1_0_io_out_column,Unit4_1_1_io_out_column}; // @[bitfusion8.scala 84:18]
  assign Bridge2_1_0_io_sgn = io_sgn; // @[bitfusion8.scala 85:12]
  assign Bridge2_1_0_io_res_in = io_statC_in[95:80]; // @[bitfusion8.scala 281:31]
  assign Bridge2_1_1_clock = clock;
  assign Bridge2_1_1_reset = reset;
  assign Bridge2_1_1_io_pl = Unit4_1_2_io_out_p; // @[bitfusion8.scala 81:11]
  assign Bridge2_1_1_io_pr = Unit4_1_3_io_out_p; // @[bitfusion8.scala 82:11]
  assign Bridge2_1_1_io_in_row = Unit4_1_2_io_out_row; // @[bitfusion8.scala 83:15]
  assign Bridge2_1_1_io_in_column = {Unit4_1_2_io_out_column,Unit4_1_3_io_out_column}; // @[bitfusion8.scala 84:18]
  assign Bridge2_1_1_io_sgn = io_sgn; // @[bitfusion8.scala 85:12]
  assign Bridge2_1_1_io_res_in = io_statC_in[79:64]; // @[bitfusion8.scala 281:31]
  assign Bridge2_2_0_clock = clock;
  assign Bridge2_2_0_reset = reset;
  assign Bridge2_2_0_io_pl = Unit4_2_0_io_out_p; // @[bitfusion8.scala 81:11]
  assign Bridge2_2_0_io_pr = Unit4_2_1_io_out_p; // @[bitfusion8.scala 82:11]
  assign Bridge2_2_0_io_in_row = Unit4_2_0_io_out_row; // @[bitfusion8.scala 83:15]
  assign Bridge2_2_0_io_in_column = {Unit4_2_0_io_out_column,Unit4_2_1_io_out_column}; // @[bitfusion8.scala 84:18]
  assign Bridge2_2_0_io_sgn = io_sgn; // @[bitfusion8.scala 85:12]
  assign Bridge2_2_0_io_res_in = io_statC_in[63:48]; // @[bitfusion8.scala 281:31]
  assign Bridge2_2_1_clock = clock;
  assign Bridge2_2_1_reset = reset;
  assign Bridge2_2_1_io_pl = Unit4_2_2_io_out_p; // @[bitfusion8.scala 81:11]
  assign Bridge2_2_1_io_pr = Unit4_2_3_io_out_p; // @[bitfusion8.scala 82:11]
  assign Bridge2_2_1_io_in_row = Unit4_2_2_io_out_row; // @[bitfusion8.scala 83:15]
  assign Bridge2_2_1_io_in_column = {Unit4_2_2_io_out_column,Unit4_2_3_io_out_column}; // @[bitfusion8.scala 84:18]
  assign Bridge2_2_1_io_sgn = io_sgn; // @[bitfusion8.scala 85:12]
  assign Bridge2_2_1_io_res_in = io_statC_in[47:32]; // @[bitfusion8.scala 281:31]
  assign Bridge2_3_0_clock = clock;
  assign Bridge2_3_0_reset = reset;
  assign Bridge2_3_0_io_pl = Unit4_3_0_io_out_p; // @[bitfusion8.scala 81:11]
  assign Bridge2_3_0_io_pr = Unit4_3_1_io_out_p; // @[bitfusion8.scala 82:11]
  assign Bridge2_3_0_io_in_row = Unit4_3_0_io_out_row; // @[bitfusion8.scala 83:15]
  assign Bridge2_3_0_io_in_column = {Unit4_3_0_io_out_column,Unit4_3_1_io_out_column}; // @[bitfusion8.scala 84:18]
  assign Bridge2_3_0_io_sgn = io_sgn; // @[bitfusion8.scala 85:12]
  assign Bridge2_3_0_io_res_in = io_statC_in[31:16]; // @[bitfusion8.scala 281:31]
  assign Bridge2_3_1_clock = clock;
  assign Bridge2_3_1_reset = reset;
  assign Bridge2_3_1_io_pl = Unit4_3_2_io_out_p; // @[bitfusion8.scala 81:11]
  assign Bridge2_3_1_io_pr = Unit4_3_3_io_out_p; // @[bitfusion8.scala 82:11]
  assign Bridge2_3_1_io_in_row = Unit4_3_2_io_out_row; // @[bitfusion8.scala 83:15]
  assign Bridge2_3_1_io_in_column = {Unit4_3_2_io_out_column,Unit4_3_3_io_out_column}; // @[bitfusion8.scala 84:18]
  assign Bridge2_3_1_io_sgn = io_sgn; // @[bitfusion8.scala 85:12]
  assign Bridge2_3_1_io_res_in = io_statC_in[15:0]; // @[bitfusion8.scala 281:31]
  assign Bridge4_col_0_0_clock = clock;
  assign Bridge4_col_0_0_reset = reset;
  assign Bridge4_col_0_0_io_pl = Bridge2_0_0_io_out_p; // @[bitfusion8.scala 133:11]
  assign Bridge4_col_0_0_io_pr = Bridge2_1_0_io_out_p; // @[bitfusion8.scala 134:11]
  assign Bridge4_col_0_0_io_in_row = {Bridge2_0_0_io_out_row,Bridge2_1_0_io_out_row}; // @[bitfusion8.scala 135:15]
  assign Bridge4_col_0_0_io_in_column = Bridge2_0_0_io_out_column; // @[bitfusion8.scala 136:18]
  assign Bridge4_col_0_0_io_sgn = io_sgn; // @[bitfusion8.scala 137:12]
  assign Bridge4_col_0_0_io_res_in = io_statC_in[127:96]; // @[bitfusion8.scala 286:35]
  assign Bridge4_col_0_1_clock = clock;
  assign Bridge4_col_0_1_reset = reset;
  assign Bridge4_col_0_1_io_pl = Bridge2_0_1_io_out_p; // @[bitfusion8.scala 133:11]
  assign Bridge4_col_0_1_io_pr = Bridge2_1_1_io_out_p; // @[bitfusion8.scala 134:11]
  assign Bridge4_col_0_1_io_in_row = {Bridge2_0_1_io_out_row,Bridge2_1_1_io_out_row}; // @[bitfusion8.scala 135:15]
  assign Bridge4_col_0_1_io_in_column = Bridge2_0_1_io_out_column; // @[bitfusion8.scala 136:18]
  assign Bridge4_col_0_1_io_sgn = io_sgn; // @[bitfusion8.scala 137:12]
  assign Bridge4_col_0_1_io_res_in = io_statC_in[95:64]; // @[bitfusion8.scala 286:35]
  assign Bridge4_col_1_0_clock = clock;
  assign Bridge4_col_1_0_reset = reset;
  assign Bridge4_col_1_0_io_pl = Bridge2_2_0_io_out_p; // @[bitfusion8.scala 133:11]
  assign Bridge4_col_1_0_io_pr = Bridge2_3_0_io_out_p; // @[bitfusion8.scala 134:11]
  assign Bridge4_col_1_0_io_in_row = {Bridge2_2_0_io_out_row,Bridge2_3_0_io_out_row}; // @[bitfusion8.scala 135:15]
  assign Bridge4_col_1_0_io_in_column = Bridge2_2_0_io_out_column; // @[bitfusion8.scala 136:18]
  assign Bridge4_col_1_0_io_sgn = io_sgn; // @[bitfusion8.scala 137:12]
  assign Bridge4_col_1_0_io_res_in = io_statC_in[63:32]; // @[bitfusion8.scala 286:35]
  assign Bridge4_col_1_1_clock = clock;
  assign Bridge4_col_1_1_reset = reset;
  assign Bridge4_col_1_1_io_pl = Bridge2_2_1_io_out_p; // @[bitfusion8.scala 133:11]
  assign Bridge4_col_1_1_io_pr = Bridge2_3_1_io_out_p; // @[bitfusion8.scala 134:11]
  assign Bridge4_col_1_1_io_in_row = {Bridge2_2_1_io_out_row,Bridge2_3_1_io_out_row}; // @[bitfusion8.scala 135:15]
  assign Bridge4_col_1_1_io_in_column = Bridge2_2_1_io_out_column; // @[bitfusion8.scala 136:18]
  assign Bridge4_col_1_1_io_sgn = io_sgn; // @[bitfusion8.scala 137:12]
  assign Bridge4_col_1_1_io_res_in = io_statC_in[31:0]; // @[bitfusion8.scala 286:35]
  assign Bridge8_0_clock = clock;
  assign Bridge8_0_reset = reset;
  assign Bridge8_0_io_pl = Bridge4_col_0_0_io_out_p; // @[bitfusion8.scala 81:11]
  assign Bridge8_0_io_pr = Bridge4_col_0_1_io_out_p; // @[bitfusion8.scala 82:11]
  assign Bridge8_0_io_in_row = Bridge4_col_0_0_io_out_row; // @[bitfusion8.scala 83:15]
  assign Bridge8_0_io_in_column = {Bridge4_col_0_0_io_out_column,Bridge4_col_0_1_io_out_column}; // @[bitfusion8.scala 84:18]
  assign Bridge8_0_io_sgn = io_sgn; // @[bitfusion8.scala 85:12]
  assign Bridge8_0_io_res_in = io_statC_in[63:32]; // @[bitfusion8.scala 290:26]
  assign Bridge8_1_clock = clock;
  assign Bridge8_1_reset = reset;
  assign Bridge8_1_io_pl = Bridge4_col_1_0_io_out_p; // @[bitfusion8.scala 81:11]
  assign Bridge8_1_io_pr = Bridge4_col_1_1_io_out_p; // @[bitfusion8.scala 82:11]
  assign Bridge8_1_io_in_row = Bridge4_col_1_0_io_out_row; // @[bitfusion8.scala 83:15]
  assign Bridge8_1_io_in_column = {Bridge4_col_1_0_io_out_column,Bridge4_col_1_1_io_out_column}; // @[bitfusion8.scala 84:18]
  assign Bridge8_1_io_sgn = io_sgn; // @[bitfusion8.scala 85:12]
  assign Bridge8_1_io_res_in = io_statC_in[31:0]; // @[bitfusion8.scala 290:26]
  assign Bridge16_clock = clock;
  assign Bridge16_reset = reset;
  assign Bridge16_io_pl = Bridge8_0_io_out_p; // @[bitfusion8.scala 81:11]
  assign Bridge16_io_pr = Bridge8_1_io_out_p; // @[bitfusion8.scala 82:11]
  assign Bridge16_io_in_row = {Bridge8_0_io_out_row,Bridge8_1_io_out_row}; // @[bitfusion8.scala 83:15]
  assign Bridge16_io_in_column = Bridge8_0_io_out_column; // @[bitfusion8.scala 84:18]
  assign Bridge16_io_sgn = io_sgn; // @[bitfusion8.scala 85:12]
  assign Bridge16_io_res_in = io_statC_in[31:0]; // @[bitfusion8.scala 292:22]
endmodule
module DynamicPE_Batch(
  input          clock,
  input          reset,
  input  [2:0]   io_ctrl,
  input  [15:0]  io_in_row_0,
  input  [15:0]  io_in_row_1,
  input  [15:0]  io_in_row_2,
  input  [15:0]  io_in_row_3,
  input  [15:0]  io_in_row_4,
  input  [15:0]  io_in_row_5,
  input  [15:0]  io_in_row_6,
  input  [15:0]  io_in_row_7,
  input  [15:0]  io_in_row_8,
  input  [15:0]  io_in_row_9,
  input  [15:0]  io_in_row_10,
  input  [15:0]  io_in_row_11,
  input  [15:0]  io_in_row_12,
  input  [15:0]  io_in_row_13,
  input  [15:0]  io_in_row_14,
  input  [15:0]  io_in_row_15,
  input  [15:0]  io_in_column,
  input          io_sgn,
  input  [127:0] io_statC_in_0,
  input  [127:0] io_statC_in_1,
  input  [127:0] io_statC_in_2,
  input  [127:0] io_statC_in_3,
  input  [127:0] io_statC_in_4,
  input  [127:0] io_statC_in_5,
  input  [127:0] io_statC_in_6,
  input  [127:0] io_statC_in_7,
  input  [127:0] io_statC_in_8,
  input  [127:0] io_statC_in_9,
  input  [127:0] io_statC_in_10,
  input  [127:0] io_statC_in_11,
  input  [127:0] io_statC_in_12,
  input  [127:0] io_statC_in_13,
  input  [127:0] io_statC_in_14,
  input  [127:0] io_statC_in_15,
  output [127:0] io_statC_out_0,
  output [127:0] io_statC_out_1,
  output [127:0] io_statC_out_2,
  output [127:0] io_statC_out_3,
  output [127:0] io_statC_out_4,
  output [127:0] io_statC_out_5,
  output [127:0] io_statC_out_6,
  output [127:0] io_statC_out_7,
  output [127:0] io_statC_out_8,
  output [127:0] io_statC_out_9,
  output [127:0] io_statC_out_10,
  output [127:0] io_statC_out_11,
  output [127:0] io_statC_out_12,
  output [127:0] io_statC_out_13,
  output [127:0] io_statC_out_14,
  output [127:0] io_statC_out_15
);
  wire  pe_0_clock; // @[bitfusion8.scala 331:11]
  wire  pe_0_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_0_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_0_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_0_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_0_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_0_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_0_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_1_clock; // @[bitfusion8.scala 331:11]
  wire  pe_1_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_1_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_1_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_1_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_1_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_1_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_1_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_2_clock; // @[bitfusion8.scala 331:11]
  wire  pe_2_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_2_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_2_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_2_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_2_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_2_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_2_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_3_clock; // @[bitfusion8.scala 331:11]
  wire  pe_3_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_3_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_3_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_3_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_3_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_3_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_3_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_4_clock; // @[bitfusion8.scala 331:11]
  wire  pe_4_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_4_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_4_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_4_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_4_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_4_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_4_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_5_clock; // @[bitfusion8.scala 331:11]
  wire  pe_5_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_5_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_5_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_5_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_5_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_5_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_5_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_6_clock; // @[bitfusion8.scala 331:11]
  wire  pe_6_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_6_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_6_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_6_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_6_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_6_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_6_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_7_clock; // @[bitfusion8.scala 331:11]
  wire  pe_7_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_7_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_7_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_7_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_7_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_7_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_7_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_8_clock; // @[bitfusion8.scala 331:11]
  wire  pe_8_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_8_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_8_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_8_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_8_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_8_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_8_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_9_clock; // @[bitfusion8.scala 331:11]
  wire  pe_9_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_9_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_9_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_9_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_9_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_9_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_9_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_10_clock; // @[bitfusion8.scala 331:11]
  wire  pe_10_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_10_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_10_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_10_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_10_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_10_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_10_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_11_clock; // @[bitfusion8.scala 331:11]
  wire  pe_11_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_11_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_11_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_11_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_11_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_11_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_11_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_12_clock; // @[bitfusion8.scala 331:11]
  wire  pe_12_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_12_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_12_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_12_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_12_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_12_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_12_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_13_clock; // @[bitfusion8.scala 331:11]
  wire  pe_13_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_13_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_13_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_13_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_13_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_13_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_13_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_14_clock; // @[bitfusion8.scala 331:11]
  wire  pe_14_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_14_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_14_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_14_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_14_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_14_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_14_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  pe_15_clock; // @[bitfusion8.scala 331:11]
  wire  pe_15_reset; // @[bitfusion8.scala 331:11]
  wire [2:0] pe_15_io_ctrl; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_15_io_in_row; // @[bitfusion8.scala 331:11]
  wire [15:0] pe_15_io_in_column; // @[bitfusion8.scala 331:11]
  wire  pe_15_io_sgn; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_15_io_statC_in; // @[bitfusion8.scala 331:11]
  wire [127:0] pe_15_io_statC_out; // @[bitfusion8.scala 331:11]
  wire  _T; // @[bitfusion8.scala 337:39]
  wire  _T_1; // @[bitfusion8.scala 337:39]
  wire  _T_2; // @[bitfusion8.scala 337:39]
  wire  _T_3; // @[bitfusion8.scala 337:39]
  wire  _T_4; // @[bitfusion8.scala 337:39]
  wire  _T_5; // @[bitfusion8.scala 337:39]
  wire  _T_6; // @[bitfusion8.scala 337:39]
  wire  _T_7; // @[bitfusion8.scala 337:39]
  wire  _T_8; // @[bitfusion8.scala 337:39]
  wire  _T_9; // @[bitfusion8.scala 337:39]
  wire  _T_10; // @[bitfusion8.scala 337:39]
  wire  _T_11; // @[bitfusion8.scala 337:39]
  wire  _T_12; // @[bitfusion8.scala 337:39]
  wire  _T_13; // @[bitfusion8.scala 337:39]
  wire  _T_14; // @[bitfusion8.scala 337:39]
  wire  _T_15; // @[bitfusion8.scala 337:39]
  DynamicPE_WS pe_0 ( // @[bitfusion8.scala 331:11]
    .clock(pe_0_clock),
    .reset(pe_0_reset),
    .io_ctrl(pe_0_io_ctrl),
    .io_in_row(pe_0_io_in_row),
    .io_in_column(pe_0_io_in_column),
    .io_sgn(pe_0_io_sgn),
    .io_statC_in(pe_0_io_statC_in),
    .io_statC_out(pe_0_io_statC_out)
  );
  DynamicPE_WS pe_1 ( // @[bitfusion8.scala 331:11]
    .clock(pe_1_clock),
    .reset(pe_1_reset),
    .io_ctrl(pe_1_io_ctrl),
    .io_in_row(pe_1_io_in_row),
    .io_in_column(pe_1_io_in_column),
    .io_sgn(pe_1_io_sgn),
    .io_statC_in(pe_1_io_statC_in),
    .io_statC_out(pe_1_io_statC_out)
  );
  DynamicPE_WS pe_2 ( // @[bitfusion8.scala 331:11]
    .clock(pe_2_clock),
    .reset(pe_2_reset),
    .io_ctrl(pe_2_io_ctrl),
    .io_in_row(pe_2_io_in_row),
    .io_in_column(pe_2_io_in_column),
    .io_sgn(pe_2_io_sgn),
    .io_statC_in(pe_2_io_statC_in),
    .io_statC_out(pe_2_io_statC_out)
  );
  DynamicPE_WS pe_3 ( // @[bitfusion8.scala 331:11]
    .clock(pe_3_clock),
    .reset(pe_3_reset),
    .io_ctrl(pe_3_io_ctrl),
    .io_in_row(pe_3_io_in_row),
    .io_in_column(pe_3_io_in_column),
    .io_sgn(pe_3_io_sgn),
    .io_statC_in(pe_3_io_statC_in),
    .io_statC_out(pe_3_io_statC_out)
  );
  DynamicPE_WS pe_4 ( // @[bitfusion8.scala 331:11]
    .clock(pe_4_clock),
    .reset(pe_4_reset),
    .io_ctrl(pe_4_io_ctrl),
    .io_in_row(pe_4_io_in_row),
    .io_in_column(pe_4_io_in_column),
    .io_sgn(pe_4_io_sgn),
    .io_statC_in(pe_4_io_statC_in),
    .io_statC_out(pe_4_io_statC_out)
  );
  DynamicPE_WS pe_5 ( // @[bitfusion8.scala 331:11]
    .clock(pe_5_clock),
    .reset(pe_5_reset),
    .io_ctrl(pe_5_io_ctrl),
    .io_in_row(pe_5_io_in_row),
    .io_in_column(pe_5_io_in_column),
    .io_sgn(pe_5_io_sgn),
    .io_statC_in(pe_5_io_statC_in),
    .io_statC_out(pe_5_io_statC_out)
  );
  DynamicPE_WS pe_6 ( // @[bitfusion8.scala 331:11]
    .clock(pe_6_clock),
    .reset(pe_6_reset),
    .io_ctrl(pe_6_io_ctrl),
    .io_in_row(pe_6_io_in_row),
    .io_in_column(pe_6_io_in_column),
    .io_sgn(pe_6_io_sgn),
    .io_statC_in(pe_6_io_statC_in),
    .io_statC_out(pe_6_io_statC_out)
  );
  DynamicPE_WS pe_7 ( // @[bitfusion8.scala 331:11]
    .clock(pe_7_clock),
    .reset(pe_7_reset),
    .io_ctrl(pe_7_io_ctrl),
    .io_in_row(pe_7_io_in_row),
    .io_in_column(pe_7_io_in_column),
    .io_sgn(pe_7_io_sgn),
    .io_statC_in(pe_7_io_statC_in),
    .io_statC_out(pe_7_io_statC_out)
  );
  DynamicPE_WS pe_8 ( // @[bitfusion8.scala 331:11]
    .clock(pe_8_clock),
    .reset(pe_8_reset),
    .io_ctrl(pe_8_io_ctrl),
    .io_in_row(pe_8_io_in_row),
    .io_in_column(pe_8_io_in_column),
    .io_sgn(pe_8_io_sgn),
    .io_statC_in(pe_8_io_statC_in),
    .io_statC_out(pe_8_io_statC_out)
  );
  DynamicPE_WS pe_9 ( // @[bitfusion8.scala 331:11]
    .clock(pe_9_clock),
    .reset(pe_9_reset),
    .io_ctrl(pe_9_io_ctrl),
    .io_in_row(pe_9_io_in_row),
    .io_in_column(pe_9_io_in_column),
    .io_sgn(pe_9_io_sgn),
    .io_statC_in(pe_9_io_statC_in),
    .io_statC_out(pe_9_io_statC_out)
  );
  DynamicPE_WS pe_10 ( // @[bitfusion8.scala 331:11]
    .clock(pe_10_clock),
    .reset(pe_10_reset),
    .io_ctrl(pe_10_io_ctrl),
    .io_in_row(pe_10_io_in_row),
    .io_in_column(pe_10_io_in_column),
    .io_sgn(pe_10_io_sgn),
    .io_statC_in(pe_10_io_statC_in),
    .io_statC_out(pe_10_io_statC_out)
  );
  DynamicPE_WS pe_11 ( // @[bitfusion8.scala 331:11]
    .clock(pe_11_clock),
    .reset(pe_11_reset),
    .io_ctrl(pe_11_io_ctrl),
    .io_in_row(pe_11_io_in_row),
    .io_in_column(pe_11_io_in_column),
    .io_sgn(pe_11_io_sgn),
    .io_statC_in(pe_11_io_statC_in),
    .io_statC_out(pe_11_io_statC_out)
  );
  DynamicPE_WS pe_12 ( // @[bitfusion8.scala 331:11]
    .clock(pe_12_clock),
    .reset(pe_12_reset),
    .io_ctrl(pe_12_io_ctrl),
    .io_in_row(pe_12_io_in_row),
    .io_in_column(pe_12_io_in_column),
    .io_sgn(pe_12_io_sgn),
    .io_statC_in(pe_12_io_statC_in),
    .io_statC_out(pe_12_io_statC_out)
  );
  DynamicPE_WS pe_13 ( // @[bitfusion8.scala 331:11]
    .clock(pe_13_clock),
    .reset(pe_13_reset),
    .io_ctrl(pe_13_io_ctrl),
    .io_in_row(pe_13_io_in_row),
    .io_in_column(pe_13_io_in_column),
    .io_sgn(pe_13_io_sgn),
    .io_statC_in(pe_13_io_statC_in),
    .io_statC_out(pe_13_io_statC_out)
  );
  DynamicPE_WS pe_14 ( // @[bitfusion8.scala 331:11]
    .clock(pe_14_clock),
    .reset(pe_14_reset),
    .io_ctrl(pe_14_io_ctrl),
    .io_in_row(pe_14_io_in_row),
    .io_in_column(pe_14_io_in_column),
    .io_sgn(pe_14_io_sgn),
    .io_statC_in(pe_14_io_statC_in),
    .io_statC_out(pe_14_io_statC_out)
  );
  DynamicPE_WS pe_15 ( // @[bitfusion8.scala 331:11]
    .clock(pe_15_clock),
    .reset(pe_15_reset),
    .io_ctrl(pe_15_io_ctrl),
    .io_in_row(pe_15_io_in_row),
    .io_in_column(pe_15_io_in_column),
    .io_sgn(pe_15_io_sgn),
    .io_statC_in(pe_15_io_statC_in),
    .io_statC_out(pe_15_io_statC_out)
  );
  assign _T = io_in_column[0]; // @[bitfusion8.scala 337:39]
  assign _T_1 = io_in_column[1]; // @[bitfusion8.scala 337:39]
  assign _T_2 = io_in_column[2]; // @[bitfusion8.scala 337:39]
  assign _T_3 = io_in_column[3]; // @[bitfusion8.scala 337:39]
  assign _T_4 = io_in_column[4]; // @[bitfusion8.scala 337:39]
  assign _T_5 = io_in_column[5]; // @[bitfusion8.scala 337:39]
  assign _T_6 = io_in_column[6]; // @[bitfusion8.scala 337:39]
  assign _T_7 = io_in_column[7]; // @[bitfusion8.scala 337:39]
  assign _T_8 = io_in_column[8]; // @[bitfusion8.scala 337:39]
  assign _T_9 = io_in_column[9]; // @[bitfusion8.scala 337:39]
  assign _T_10 = io_in_column[10]; // @[bitfusion8.scala 337:39]
  assign _T_11 = io_in_column[11]; // @[bitfusion8.scala 337:39]
  assign _T_12 = io_in_column[12]; // @[bitfusion8.scala 337:39]
  assign _T_13 = io_in_column[13]; // @[bitfusion8.scala 337:39]
  assign _T_14 = io_in_column[14]; // @[bitfusion8.scala 337:39]
  assign _T_15 = io_in_column[15]; // @[bitfusion8.scala 337:39]
  assign io_statC_out_0 = pe_0_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_1 = pe_1_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_2 = pe_2_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_3 = pe_3_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_4 = pe_4_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_5 = pe_5_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_6 = pe_6_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_7 = pe_7_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_8 = pe_8_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_9 = pe_9_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_10 = pe_10_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_11 = pe_11_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_12 = pe_12_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_13 = pe_13_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_14 = pe_14_io_statC_out; // @[bitfusion8.scala 339:21]
  assign io_statC_out_15 = pe_15_io_statC_out; // @[bitfusion8.scala 339:21]
  assign pe_0_clock = clock;
  assign pe_0_reset = reset;
  assign pe_0_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_0_io_in_row = io_in_row_0; // @[bitfusion8.scala 336:21]
  assign pe_0_io_in_column = {{15'd0}, _T}; // @[bitfusion8.scala 337:24]
  assign pe_0_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_0_io_statC_in = io_statC_in_0; // @[bitfusion8.scala 338:23]
  assign pe_1_clock = clock;
  assign pe_1_reset = reset;
  assign pe_1_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_1_io_in_row = io_in_row_1; // @[bitfusion8.scala 336:21]
  assign pe_1_io_in_column = {{15'd0}, _T_1}; // @[bitfusion8.scala 337:24]
  assign pe_1_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_1_io_statC_in = io_statC_in_1; // @[bitfusion8.scala 338:23]
  assign pe_2_clock = clock;
  assign pe_2_reset = reset;
  assign pe_2_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_2_io_in_row = io_in_row_2; // @[bitfusion8.scala 336:21]
  assign pe_2_io_in_column = {{15'd0}, _T_2}; // @[bitfusion8.scala 337:24]
  assign pe_2_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_2_io_statC_in = io_statC_in_2; // @[bitfusion8.scala 338:23]
  assign pe_3_clock = clock;
  assign pe_3_reset = reset;
  assign pe_3_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_3_io_in_row = io_in_row_3; // @[bitfusion8.scala 336:21]
  assign pe_3_io_in_column = {{15'd0}, _T_3}; // @[bitfusion8.scala 337:24]
  assign pe_3_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_3_io_statC_in = io_statC_in_3; // @[bitfusion8.scala 338:23]
  assign pe_4_clock = clock;
  assign pe_4_reset = reset;
  assign pe_4_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_4_io_in_row = io_in_row_4; // @[bitfusion8.scala 336:21]
  assign pe_4_io_in_column = {{15'd0}, _T_4}; // @[bitfusion8.scala 337:24]
  assign pe_4_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_4_io_statC_in = io_statC_in_4; // @[bitfusion8.scala 338:23]
  assign pe_5_clock = clock;
  assign pe_5_reset = reset;
  assign pe_5_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_5_io_in_row = io_in_row_5; // @[bitfusion8.scala 336:21]
  assign pe_5_io_in_column = {{15'd0}, _T_5}; // @[bitfusion8.scala 337:24]
  assign pe_5_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_5_io_statC_in = io_statC_in_5; // @[bitfusion8.scala 338:23]
  assign pe_6_clock = clock;
  assign pe_6_reset = reset;
  assign pe_6_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_6_io_in_row = io_in_row_6; // @[bitfusion8.scala 336:21]
  assign pe_6_io_in_column = {{15'd0}, _T_6}; // @[bitfusion8.scala 337:24]
  assign pe_6_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_6_io_statC_in = io_statC_in_6; // @[bitfusion8.scala 338:23]
  assign pe_7_clock = clock;
  assign pe_7_reset = reset;
  assign pe_7_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_7_io_in_row = io_in_row_7; // @[bitfusion8.scala 336:21]
  assign pe_7_io_in_column = {{15'd0}, _T_7}; // @[bitfusion8.scala 337:24]
  assign pe_7_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_7_io_statC_in = io_statC_in_7; // @[bitfusion8.scala 338:23]
  assign pe_8_clock = clock;
  assign pe_8_reset = reset;
  assign pe_8_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_8_io_in_row = io_in_row_8; // @[bitfusion8.scala 336:21]
  assign pe_8_io_in_column = {{15'd0}, _T_8}; // @[bitfusion8.scala 337:24]
  assign pe_8_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_8_io_statC_in = io_statC_in_8; // @[bitfusion8.scala 338:23]
  assign pe_9_clock = clock;
  assign pe_9_reset = reset;
  assign pe_9_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_9_io_in_row = io_in_row_9; // @[bitfusion8.scala 336:21]
  assign pe_9_io_in_column = {{15'd0}, _T_9}; // @[bitfusion8.scala 337:24]
  assign pe_9_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_9_io_statC_in = io_statC_in_9; // @[bitfusion8.scala 338:23]
  assign pe_10_clock = clock;
  assign pe_10_reset = reset;
  assign pe_10_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_10_io_in_row = io_in_row_10; // @[bitfusion8.scala 336:21]
  assign pe_10_io_in_column = {{15'd0}, _T_10}; // @[bitfusion8.scala 337:24]
  assign pe_10_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_10_io_statC_in = io_statC_in_10; // @[bitfusion8.scala 338:23]
  assign pe_11_clock = clock;
  assign pe_11_reset = reset;
  assign pe_11_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_11_io_in_row = io_in_row_11; // @[bitfusion8.scala 336:21]
  assign pe_11_io_in_column = {{15'd0}, _T_11}; // @[bitfusion8.scala 337:24]
  assign pe_11_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_11_io_statC_in = io_statC_in_11; // @[bitfusion8.scala 338:23]
  assign pe_12_clock = clock;
  assign pe_12_reset = reset;
  assign pe_12_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_12_io_in_row = io_in_row_12; // @[bitfusion8.scala 336:21]
  assign pe_12_io_in_column = {{15'd0}, _T_12}; // @[bitfusion8.scala 337:24]
  assign pe_12_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_12_io_statC_in = io_statC_in_12; // @[bitfusion8.scala 338:23]
  assign pe_13_clock = clock;
  assign pe_13_reset = reset;
  assign pe_13_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_13_io_in_row = io_in_row_13; // @[bitfusion8.scala 336:21]
  assign pe_13_io_in_column = {{15'd0}, _T_13}; // @[bitfusion8.scala 337:24]
  assign pe_13_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_13_io_statC_in = io_statC_in_13; // @[bitfusion8.scala 338:23]
  assign pe_14_clock = clock;
  assign pe_14_reset = reset;
  assign pe_14_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_14_io_in_row = io_in_row_14; // @[bitfusion8.scala 336:21]
  assign pe_14_io_in_column = {{15'd0}, _T_14}; // @[bitfusion8.scala 337:24]
  assign pe_14_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_14_io_statC_in = io_statC_in_14; // @[bitfusion8.scala 338:23]
  assign pe_15_clock = clock;
  assign pe_15_reset = reset;
  assign pe_15_io_ctrl = io_ctrl; // @[bitfusion8.scala 334:19]
  assign pe_15_io_in_row = io_in_row_15; // @[bitfusion8.scala 336:21]
  assign pe_15_io_in_column = {{15'd0}, _T_15}; // @[bitfusion8.scala 337:24]
  assign pe_15_io_sgn = io_sgn; // @[bitfusion8.scala 335:18]
  assign pe_15_io_statC_in = io_statC_in_15; // @[bitfusion8.scala 338:23]
endmodule
