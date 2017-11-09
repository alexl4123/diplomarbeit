package Game;

import BackgroundMatrix.BackgroundGrid;
import BackgroundMatrix.Move;
import java.io.PrintStream;
import java.util.ArrayList;













































public class AILogic
{
  private BackgroundGrid _WorkPos;
  private BackgroundGrid _BestPos;
  public int MaxDepth;
  public int _BestMove;
  public ArrayList<MovePos> BestMove = new ArrayList();
  



  public int loop;
  



  private int count;
  




  public AILogic()
  {
    loop = 0;
  }
  









  public BackgroundGrid playGame(BackgroundGrid BGG2, boolean Team)
  {
    _WorkPos = BGG2;
    _BestPos = null;
    
    Float x = Float.valueOf(alphaBeta(0, _WorkPos, Team));
    
    return BGG2;
  }
  













  public float alphaBeta(int depth, BackgroundGrid BGG2, boolean Team)
  {
    MaxDepth = depth;
    count = 0;
    float beta = 10000.0F;
    for (int i = 1; i <= depth; i++) {
      MaxDepth = i;
      beta = alphaBetaHelper(0, BGG2, Team, 100000.0F, -beta);
      System.out.println(beta);
    }
    
    return beta;
  }
  
















  public float alphaBetaHelper(int depth, BackgroundGrid BGG2, boolean Team, float alpha, float beta)
  {
    float Sum = boardEvaluation(BGG2, Team);
    if (Sum > 5000.0F) {
      return 20000.0F;
    }
    
    if (depth >= MaxDepth)
    {
      return Sum;
    }
    
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        int iPos = iBackground[x][y];
        if (iPos > 0) {
          Move M = new Move();
          M.setBGG(iBackground);
          M.setBGG2(BGG2);
          
          ArrayList<MovePos> AIM = M.getMoveMeeple(iBackground, Team, iPos, x, y);
          for (MovePos A : AIM)
          {
            iBackground[PX][PY] = ID;
            iBackground[X][Y] = 0;
            if (ID3 > 0) {
              iBackground[X3][Y3] = 0;
            }
            
            if (ID4 > 0) {
              iBackground[X4][Y4] = 0;
              if (X3 > 0) {
                iBackground[X3][Y3] = 0;
              }
              iBackground[X5][Y5] = ID4;
              BGG2.setbRookMoved(ID, true);
              BGG2.setbKingMoved(ID, true);
            }
            
            if ((ID >= 100) && (ID < 110) && (Team) && (PY == 7)) {
              iBackground[PX][PY] = (140 + BGG2.getQueenNumber());
            } else if ((ID >= 200) && (ID < 210) && (!Team) && (PY == 0)) {
              iBackground[PX][PY] = (240 + BGG2.getQueenNumber());
            }
            

            BGG2.changeTeam();
            float Sum1 = -alphaBetaHelper(depth + 1, BGG2, !Team, -beta, -alpha);
            



            iBackground[PX][PY] = ID2;
            iBackground[X][Y] = ID;
            if (ID3 > 0) {
              iBackground[X3][Y3] = ID3;
            }
            
            if (ID4 > 0) {
              iBackground[X4][Y4] = ID4;
              if (X3 > 0) {
                iBackground[X3][Y3] = ID3;
              }
              iBackground[X5][Y5] = ID5;
              BGG2.setbRookMoved(ID, false);
              BGG2.setbKingMoved(ID, false);
            }
            
            if ((ID >= 100) && (ID < 110) && (Team) && (PY == 7)) {
              iBackground[PX][PY] = ID2;
            } else if ((ID >= 200) && (ID < 210) && (!Team) && (PY == 0)) {
              iBackground[PX][PY] = ID2;
            }
            



            BGG2.changeTeam();
            
            if (Sum1 > beta)
            {
              beta = Sum1;
              if (Sum1 >= alpha)
              {

                return alpha;
              }
              if (depth == 0) {
                loop += 1;
                BestMove.add(A);
                
                _BestMove = loop;
              }
            }
          }
        }
      }
    }
    



    return beta;
  }
  










  public float boardEvaluation(BackgroundGrid BGG2, boolean Team)
  {
    float S1 = 0.0F;
    float S2 = 0.0F;
    float S = 0.0F;
    int[][] Board = iBackground;
    

    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        int SB = Board[x][y];
        
        if ((SB >= 100) && (SB < 110)) {
          try {
            S1 += 100.0F;
            S1 += WhitePawnSquareTable[x][y];
            
            if ((y - 1 >= 0) && (y + 1 < 8)) {
              if ((Board[x][(y - 1)] > 100) && (Board[x][(y - 1)] < 110)) {
                S1 -= 30.0F;
              } else if ((Board[x][(y + 1)] > 200) && (Board[x][(y + 1)] < 210)) {
                S1 -= 20.0F;
              }
            }
            




            if ((x - 1 >= 0) && (y + 1 < 8) && 
              (Board[(x - 1)][(y + 1)] > 100) && (Board[(x - 1)][(y + 1)] < 110)) {
              S1 += 40.0F;
            }
            
            if ((x + 1 >= 8) || (y + 1 >= 8) || 
              (Board[(x + 1)][(y + 1)] <= 100) || (Board[(x + 1)][(y + 1)] >= 110)) break label495;
            S1 += 40.0F;

          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }
        }
        else if ((SB >= 110) && (SB < 120)) {
          S1 += 500.0F;
          S1 += WhiteRookSquareTable[x][y];
        } else if ((SB >= 120) && (SB < 130)) {
          S1 += 325.0F;
          S1 += WhiteKnightSquareTable[x][y];
        } else if ((SB >= 130) && (SB < 140)) {
          S1 += 300.0F;
          S1 += WhiteBishopSquareTable[x][y];
        } else if ((SB >= 140) && (SB < 150)) {
          S1 += 900.0F;
          S1 += WhiteQueenSquareTable[x][y];
        } else if (SB == 150) {
          S1 += 10000.0F;
          if (S1 > 11000.0F) {
            S1 += WhiteKingMiddleSquareTable[x][y];
          } else {
            S1 += WhiteKingEndSquareTable[x][y];
          }
        }
        
        label495:
        if ((SB >= 200) && (SB < 210)) {
          try {
            S2 += 100.0F;
            S2 += BlackPawnSquareTable[x][y];
            
            if ((y + 1 < 8) && (y - 1 >= 0)) {
              if ((Board[x][(y + 1)] >= 200) && (Board[x][(y + 1)] < 210)) {
                S2 -= 30.0F;
              } else if ((Board[x][(y - 1)] > 100) && (Board[x][(y - 1)] < 110)) {
                S2 -= 20.0F;
              }
            }
            




            if ((x - 1 >= 0) && (y - 1 >= 0) && 
              (Board[(x - 1)][(y - 1)] > 200) && (Board[(x - 1)][(y - 1)] < 210)) {
              S2 += 40.0F;
            }
            
            if ((x + 1 >= 8) || (y - 1 < 0) || 
              (Board[(x + 1)][(y - 1)] <= 200) || (Board[(x + 1)][(y - 1)] >= 210)) continue;
            S2 += 40.0F;

          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }
        }
        else if ((SB >= 210) && (SB < 220)) {
          S2 += 500.0F;
          S2 += BlackRookSquareTable[x][y];
        } else if ((SB >= 220) && (SB < 230)) {
          S2 += 325.0F;
          S2 += BlackKnightSquareTable[x][y];
        } else if ((SB >= 230) && (SB < 240)) {
          S2 += 300.0F;
          S2 += BlackKnightSquareTable[x][y];
        } else if ((SB >= 240) && (SB < 250)) {
          S2 += 900.0F;
          S2 += BlackQueenSquareTable[x][y];
        } else if (SB == 250) {
          S2 += 10000.0F;
          if (S2 > 11000.0F) {
            S2 += BlackKingMiddleSquareTable[x][y];
          } else {
            S2 += BlackKingEndSquareTable[x][y];
          }
        }
      }
    }
    





    if (Team) {
      S = S1 - S2;
    } else
      S = S2 - S1;
    return S;
  }
  







  private int[][] WhitePawnTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { 0, 0, 0, 0, 0, 0, 0, 0, 50, 50, 50, 50, 50, 50, 50, 50, 10, 10, 20, 30, 30, 20, 10, 
      10, 5, 5, 10, 25, 25, 10, 5, 5, 0, 0, 0, 20, 20, 0, 0, 0, 5, -5, -10, 0, 0, -10, -5, 5, 5, 10, 10, -20, 
      -20, 10, 10, 5 };
    

    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] WhitePawnSquareTable = WhitePawnTable();
  







  private int[][] BlackPawnTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { 0, 0, 0, 0, 0, 0, 0, 0, 5, 10, 10, -20, -20, 10, 10, 5, 5, -5, -10, 0, 0, -10, -5, 5, 
      0, 0, 0, 20, 20, 0, 0, 0, 5, 5, 10, 25, 25, 10, 5, 5, 10, 10, 20, 30, 30, 20, 10, 10, 50, 50, 50, 50, 
      50, 50, 50, 50 };
    
    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] BlackPawnSquareTable = BlackPawnTable();
  







  private int[][] WhiteKnightTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { -50, -40, -30, -30, -30, -30, -40, -50, -40, -20, 0, 0, 0, 0, -20, -40, -30, 0, 10, 
      15, 15, 10, 0, -30, -30, 5, 15, 20, 20, 15, 5, -30, -30, 0, 15, 20, 20, 15, 0, -30, -30, 5, 10, 15, 15, 
      10, 5, -30, -40, -20, 0, 5, 5, 0, -20, -40, -50, -40, -30, -30, -30, -30, -40, -50 };
    
    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] WhiteKnightSquareTable = WhiteKnightTable();
  







  private int[][] BlackKnightTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { -50, -40, -30, -30, -30, -30, -40, -50, -40, -20, 0, 5, 5, 0, -20, -40, -30, 5, 10, 
      15, 15, 10, 5, -30, -30, 0, 15, 20, 20, 15, 0, -30, -30, 5, 15, 20, 20, 15, 5, -30, -30, 0, 10, 15, 15, 
      10, 0, -30, -40, -20, 0, 0, 0, 0, -20, -40, -50, -40, -30, -30, -30, -30, -40, -50 };
    
    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] BlackKnightSquareTable = BlackKnightTable();
  







  private int[][] WhiteBishopTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { -20, -10, -10, -10, -10, -10, -10, -20, -10, 0, 0, 0, 0, 0, 0, -10, -10, 0, 5, 10, 10, 
      5, 0, -10, -10, 5, 5, 10, 10, 5, 5, -10, -10, 0, 10, 10, 10, 10, 0, -10, -10, 10, 10, 10, 10, 10, 10, 
      -10, -10, 5, 0, 0, 0, 0, 5, -10, -20, -10, -10, -10, -10, -10, -10, -20 };
    
    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] WhiteBishopSquareTable = WhiteBishopTable();
  







  private int[][] BlackBishopTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { -20, -10, -10, -10, -10, -10, -10, -20, -10, 5, 0, 0, 0, 0, 5, -10, -10, 10, 10, 10, 
      10, 10, 10, -10, -10, 0, 10, 10, 10, 10, 0, -10, -10, 5, 5, 10, 10, 5, 5, -10, -10, 0, 5, 10, 10, 5, 
      0, -10, -10, 0, 0, 0, 0, 0, 0, -10, -20, -10, -10, -10, -10, -10, -10, -20 };
    
    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] BlackBishopSquareTable = BlackBishopTable();
  








  private int[][] WhiteRookTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { 0, 0, 0, 0, 0, 0, 0, 0, 5, 10, 10, 10, 10, 10, 10, 5, -5, 0, 0, 0, 0, 0, 0, -5, -5, 
      0, 0, 0, 0, 0, 0, -5, -5, 0, 0, 0, 0, 0, 0, -5, -5, 0, 0, 0, 0, 0, 0, -5, -5, 0, 0, 0, 0, 0, 0, -5, 
      0, 0, 0, 5, 5 };
    
    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] WhiteRookSquareTable = WhiteRookTable();
  







  private int[][] BlackRookTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { 0, 0, 0, 5, 5, 0, 0, 0, -5, 0, 0, 0, 0, 0, 0, -5, -5, 0, 0, 0, 0, 0, 0, -5, -5, 
      0, 0, 0, 0, 0, 0, -5, -5, 0, 0, 0, 0, 0, 0, -5, -5, 0, 0, 0, 0, 0, 0, -5, 5, 10, 10, 10, 10, 10, 10, 5 };
    

    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] BlackRookSquareTable = BlackRookTable();
  








  private int[][] WhiteQueenTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { -20, -10, -10, -5, -5, -10, -10, -20, -10, 0, 0, 0, 0, 0, 0, -10, -10, 0, 5, 5, 5, 5, 
      0, -10, -5, 0, 5, 5, 5, 5, 0, -5, 0, 0, 5, 5, 5, 5, 0, -5, -10, 5, 5, 5, 5, 5, 0, -10, -10, 0, 5, 
      0, 0, 0, 0, -10, -20, -10, -10, -5, -5, -10, -10, -20 };
    
    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] WhiteQueenSquareTable = WhiteQueenTable();
  








  private int[][] BlackQueenTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { -20, -10, -10, -5, -5, -10, -10, -20, -10, 0, 5, 0, 0, 0, 0, -10, -10, 5, 5, 5, 5, 5, 
      0, -10, 0, 0, 5, 5, 5, 5, 0, -5, -5, 0, 5, 5, 5, 5, 0, -5, -10, 0, 5, 5, 5, 5, 0, -10, -10, 
      0, 0, 0, 0, 0, 0, -10, -20, -10, -10, -5, -5, -10, -10, -20 };
    
    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] BlackQueenSquareTable = BlackQueenTable();
  








  private int[][] WhiteKingMiddleTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { 20, 30, 10, 0, 0, 10, 30, 20, 20, 20, 0, 0, 0, 0, 20, 20, -10, -20, -20, -20, -20, 
      -20, -20, -10, -20, -30, -30, -40, -40, -30, -30, -20, -30, -40, -40, -50, -50, -40, -40, -30, -30, -40, 
      -40, -50, -50, -40, -40, -30, -30, -40, -40, -50, -50, -40, -40, -30, -30, -40, -40, -50, -50, -40, -40, 
      -30 };
    
    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] WhiteKingMiddleSquareTable = WhiteKingMiddleTable();
  








  private int[][] BlackKingMiddleTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { -30, -40, -40, -50, -50, -40, -40, -30, -30, -40, -40, -50, -50, -40, -40, -30, -30, 
      -40, -40, -50, -50, -40, -40, -30, -30, -40, -40, -50, -50, -40, -40, -30, -20, -30, -30, -40, -40, -30, 
      -30, -20, -10, -20, -20, -20, -20, -20, -20, -10, 20, 20, 0, 0, 0, 0, 20, 20, 20, 30, 10, 0, 0, 10, 30, 
      20 };
    
    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] BlackKingMiddleSquareTable = BlackKingMiddleTable();
  








  private int[][] WhiteKingEndTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { -50, -30, -30, -30, -30, -30, -30, -50, -30, -30, 0, 0, 0, 0, -30, -30, -30, -10, 20, 
      30, 30, 20, -10, -30, -30, -10, 30, 40, 40, 30, -10, -30, -30, -10, 30, 40, 40, 30, -10, -30, -30, -10, 
      20, 30, 30, 20, -10, -30, -30, -20, -10, 0, 0, -10, -20, -30, -50, -40, -30, -20, -20, -30, -40, -50 };
    
    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] WhiteKingEndSquareTable = WhiteKingEndTable();
  








  private int[][] BlackKingEndTable()
  {
    int[][] table = new int[8][8];
    int[] tableHelper = { -50, -40, -30, -20, -20, -30, -40, -50, -30, -20, -10, 0, 0, -10, -20, -30, -30, -10, 
      20, 30, 30, 20, -10, -30, -30, -10, 30, 40, 40, 30, -10, -30, -30, -10, 30, 40, 40, 30, -10, -30, -30, 
      -10, 20, 30, 30, 20, -10, -30, -30, -30, 0, 0, 0, 0, -30, -30, -50, -30, -30, -30, -30, -30, -30, -50 };
    
    int i = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        table[x][y] = tableHelper[i];
        i++;
      }
    }
    
    return table;
  }
  



  public int[][] BlackKingEndSquareTable = BlackKingEndTable();
}
