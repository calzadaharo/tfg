package tfg.generalAnalysis

class TimeProcessing() {

  def monthToDays(x: Int): Int = {
    x match  {
      case 1 => return 0;
      case 2 => return 31;
      case 3 => return 59;
      case 4 => return 90;
      case 5 => return 120;
      case 6 => return 151;
      case 7 => return 181;
      case 8 => return 212;
      case 9 => return 243;
      case 10 => return 273;
      case 11 => return 304;
      case 12 => return 334;
    };
  }

  def yearToSecond(year: Int): Int = {
    var seconds = 0;
    val years = List.range(2016,year);
    for(i <- years) {
      if (((i % 4 == 0) && (i % 100 != 0)) || ((i % 100 == 0) && (i % 400 == 0))) {
        seconds += 366 * 24 * 60 * 60;
      } else {
        seconds += 365 * 24 * 60 * 60;
      }
    };
    return seconds;
  }

  def time(rch: String) : Long = {
    val date = (rch.split('T'))(0);
    val hour = (rch.split('T'))(1);
    val year = (date.split('-'))(0).toInt;
    val month = (date.split('-'))(1).toInt;
    val day = (date.split('-'))(2).toInt;
    var days = monthToDays(month) + day - 1;
    if ((month>2) && ((year % 4 == 0) && (year % 100 != 0)) || ((year % 100 == 0) && (year % 400 == 0))) {
      days += 1;
    };
    val hours = hour.split(':')(0).toInt;
    val minutes = hour.split(':')(1).toInt;
    val seconds = hour.split(':')(2).split('+')(0).toInt;
    val years = yearToSecond(year);
    val timestamp = years + days*24*60*60 + hours*60*60 + minutes*60 + seconds;
    return timestamp;
  }
}