package object Comete {

  // Vector de valores reales ordenados en [0,1] que representa las posibles
  // opiniones de una poblacion. El primer valor siempre es 0.0 y el ultimo 1.0.
  type DistributionValues = Vector[Double]

  // Vector de probabilidades de longitud k tal que:
  //   - 0 <= Pi_k(i) <= 1  para todo i
  //   - Pi_k.sum == 1
  type Frequency = Vector[Double]

  // Una distribucion es el par (frecuencias, valores) de la misma longitud.
  // Representa la distribucion de opiniones de una poblacion.
  type Distribution = (Frequency, DistributionValues)

  // Tipo general para cualquier medida de polarizacion:
  // recibe una distribucion y devuelve un numero real.
  type MedidaPol = Distribution => Double


  def minP(f: Double => Double, min: Double, max: Double, prec: Double): Double = {
    // Caso base: el intervalo es suficientemente pequeno
    if (max - min < prec) {
      (min + max) / 2.0
    } else {
      // Dividimos el intervalo en tres partes iguales
      val m1 = min + (max - min) / 3.0
      val m2 = max - (max - min) / 3.0

      if (f(m1) < f(m2)) {
        // El minimo no puede estar en (m2, max], descartamos ese tercio
        minP(f, min, m2, prec)
      } else {
        // El minimo no puede estar en [min, m1), descartamos ese tercio
        minP(f, m1, max, prec)
      }
    }
  }

  def rhoCMT_Gen(alpha: Double, beta: Double): MedidaPol = {
    (distribucion: Distribution) => {
      val (frecuencias, valores) = distribucion

      // rho_aux(p) = sum de pi_i^alpha * |y_i - p|^beta
      def rhoAux(p: Double): Double = {
        (for {
          i <- frecuencias.indices
        } yield Math.pow(frecuencias(i), alpha) * Math.pow(Math.abs(valores(i) - p), beta)
          ).sum
      }

      // Buscamos el p en [0,1] que minimiza rhoAux
      // Usamos una precision de 1e-6 para buena aproximacion numerica
      val pOptimo = minP(rhoAux, 0.0, 1.0, 1e-6)

      rhoAux(pOptimo)
    }
  }

  def normalizar(m: MedidaPol): MedidaPol = {
    (distribucion: Distribution) => {
      val (_, valores) = distribucion

      // Construimos la frecuencia del peor caso:
      // 0.5 en el primer extremo, 0.5 en el ultimo extremo, 0.0 en los demas
      val k = valores.length
      val frecuenciasPeorCaso: Frequency = Vector.tabulate(k) { i =>
        if (i == 0 || i == k - 1) 0.5 else 0.0
      }

      val peorCaso: Distribution = (frecuenciasPeorCaso, valores)
      val polarizacionPeorCaso = m(peorCaso)

      // Evitamos division por cero (aunque matematicamente no deberia ocurrir)
      if (polarizacionPeorCaso == 0.0) 0.0
      else m(distribucion) / polarizacionPeorCaso
    }
  }
}
