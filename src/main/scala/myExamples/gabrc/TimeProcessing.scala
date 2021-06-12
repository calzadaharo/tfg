package myExamples.gabrc

class TimeProcessing() {

  def mesADias(x: Int): Int = {
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
    }
  }

  def time(rch: String) : Long = {
    val fecha = (rch.split('T'))(0)
    val hora = (rch.split('T'))(1)
    val mes = (fecha.split('-'))(1).toInt
    val dia = (fecha.split('-'))(2).toInt
    val dias = mesADias(mes) + dia - 1;
    val horas = hora.split(':')(0).toInt;
    val minutos = hora.split(':')(1).toInt;
    val segundos = hora.split(':')(2).split('+')(0).toInt;
    val timestamp = dias*24*60*60 + horas*60*60 + minutos*60 + segundos;
    return timestamp;
  }
}