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

module ALU(
  input         clock,
  input         reset,
  input  [31:0] io_a,
  input  [31:0] io_b,
  input  [3:0]  io_op,
  input  [4:0]  io_shamt,
  output [31:0] io_out,
  output        io_zero
);
  wire  _T_15;
  wire [31:0] _T_16;
  wire  _T_18;
  wire  _T_20;
  wire  _T_21;
  wire [31:0] _T_22;
  wire [31:0] _GEN_1;
  wire  _T_24;
  wire  _T_28;
  wire  _T_29;
  wire  _T_30;
  wire [31:0] _T_32;
  wire [31:0] _GEN_2;
  wire  _T_34;
  wire  _T_41;
  wire  _T_42;
  wire  _T_43;
  wire [31:0] _T_44;
  wire [31:0] _GEN_3;
  wire  _T_46;
  wire  _T_56;
  wire  _T_57;
  wire  _T_58;
  wire [32:0] _T_59;
  wire [31:0] _T_60;
  wire [31:0] _GEN_4;
  wire  _T_62;
  wire  _T_75;
  wire  _T_76;
  wire  _T_77;
  wire [32:0] _T_78;
  wire [32:0] _T_79;
  wire [31:0] _T_80;
  wire [31:0] _GEN_5;
  wire  _T_82;
  wire  _T_98;
  wire  _T_99;
  wire  _T_100;
  wire [62:0] _GEN_11;
  wire [62:0] _T_101;
  wire [62:0] _GEN_6;
  wire  _T_103;
  wire  _T_122;
  wire  _T_123;
  wire  _T_124;
  wire [31:0] _T_125;
  wire [62:0] _GEN_7;
  wire  _T_127;
  wire  _T_149;
  wire  _T_150;
  wire  _T_151;
  wire  _T_152;
  wire [62:0] _GEN_8;
  wire  _T_154;
  wire  _T_179;
  wire  _T_180;
  wire  _T_181;
  wire [62:0] _T_183;
  wire [62:0] _GEN_9;
  wire  _T_211;
  wire  _T_212;
  wire [62:0] _GEN_10;
  wire  _T_218;
  assign io_out = _GEN_10[31:0];
  assign io_zero = _T_218;
  assign _T_15 = io_op == 4'h0;
  assign _T_16 = io_a & io_b;
  assign _T_18 = io_op == 4'h1;
  assign _T_20 = _T_15 == 1'h0;
  assign _T_21 = _T_20 & _T_18;
  assign _T_22 = io_a | io_b;
  assign _GEN_1 = _T_21 ? _T_22 : _T_16;
  assign _T_24 = io_op == 4'h2;
  assign _T_28 = _T_18 == 1'h0;
  assign _T_29 = _T_20 & _T_28;
  assign _T_30 = _T_29 & _T_24;
  assign _T_32 = ~ _T_22;
  assign _GEN_2 = _T_30 ? _T_32 : _GEN_1;
  assign _T_34 = io_op == 4'h3;
  assign _T_41 = _T_24 == 1'h0;
  assign _T_42 = _T_29 & _T_41;
  assign _T_43 = _T_42 & _T_34;
  assign _T_44 = io_a ^ io_b;
  assign _GEN_3 = _T_43 ? _T_44 : _GEN_2;
  assign _T_46 = io_op == 4'h4;
  assign _T_56 = _T_34 == 1'h0;
  assign _T_57 = _T_42 & _T_56;
  assign _T_58 = _T_57 & _T_46;
  assign _T_59 = io_a + io_b;
  assign _T_60 = _T_59[31:0];
  assign _GEN_4 = _T_58 ? _T_60 : _GEN_3;
  assign _T_62 = io_op == 4'h5;
  assign _T_75 = _T_46 == 1'h0;
  assign _T_76 = _T_57 & _T_75;
  assign _T_77 = _T_76 & _T_62;
  assign _T_78 = io_a - io_b;
  assign _T_79 = $unsigned(_T_78);
  assign _T_80 = _T_79[31:0];
  assign _GEN_5 = _T_77 ? _T_80 : _GEN_4;
  assign _T_82 = io_op == 4'h6;
  assign _T_98 = _T_62 == 1'h0;
  assign _T_99 = _T_76 & _T_98;
  assign _T_100 = _T_99 & _T_82;
  assign _GEN_11 = {{31'd0}, io_b};
  assign _T_101 = _GEN_11 << io_shamt;
  assign _GEN_6 = _T_100 ? _T_101 : {{31'd0}, _GEN_5};
  assign _T_103 = io_op == 4'h7;
  assign _T_122 = _T_82 == 1'h0;
  assign _T_123 = _T_99 & _T_122;
  assign _T_124 = _T_123 & _T_103;
  assign _T_125 = io_b >> io_shamt;
  assign _GEN_7 = _T_124 ? {{31'd0}, _T_125} : _GEN_6;
  assign _T_127 = io_op == 4'h8;
  assign _T_149 = _T_103 == 1'h0;
  assign _T_150 = _T_123 & _T_149;
  assign _T_151 = _T_150 & _T_127;
  assign _T_152 = io_a < io_b;
  assign _GEN_8 = _T_151 ? {{62'd0}, _T_152} : _GEN_7;
  assign _T_154 = io_op == 4'h9;
  assign _T_179 = _T_127 == 1'h0;
  assign _T_180 = _T_150 & _T_179;
  assign _T_181 = _T_180 & _T_154;
  assign _T_183 = _GEN_11 << 5'h10;
  assign _GEN_9 = _T_181 ? _T_183 : _GEN_8;
  assign _T_211 = _T_154 == 1'h0;
  assign _T_212 = _T_180 & _T_211;
  assign _GEN_10 = _T_212 ? 63'h0 : _GEN_9;
  assign _T_218 = _T_80 == 32'h0;
endmodule
