package Game;






public class Local
{
  private boolean _team;
  




  private int _count;
  





  public Local() {}
  





  public int getCount()
  {
    return _count;
  }
  



  public boolean getTeam()
  {
    return _team;
  }
  


  public void startUpLocal()
  {
    _team = true;
  }
  


  public void setCount(int i)
  {
    _count = i;
  }
  


  public void setTeam(boolean team)
  {
    _team = team;
  }
  


  public void switchTeam()
  {
    if (_team) {
      _team = false;
    } else {
      _team = true;
    }
  }
}
