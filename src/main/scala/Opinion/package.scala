import Comete._

package object Opinion {

  // Si n es el numero de agentes, estos se identifican
  // con los enteros entre 0 y n−1
  // O sea el conjunto de Agentes A es
  // implicitamente el conjunto {0,1,2, ... , n−1}
  // Si b:BeliefConf , para cada i en Int , b[i] es un numero
  // entre 0 y 1 que indica cuanto cree el agente i en
  // la veracidad de la proposicion p
  // Si existe i: b(i)<0 o b(i)>1 b esta mal definida

  type SpecificBelief = Vector[Double]
  // Si b:SpecificBelief , para cada i en Int , b[i] es un
  // numero entre 0 y 1 que indica cuanto cree el
  // agente i en la veracidad de la proposicion p
  // El numero de agentes es b.length
  // Si existe i: b(i)<0 o b(i)>1 b esta mal definida.
  // Para i en Int\A, b(i) no tiene sentido

  type GenericBeliefConf = Int => SpecificBelief
  // si gb:GenericBelief , entonces gb(n) =b tal que
  // b:SpecificBelief

  type AgentsPolMeasure = (SpecificBelief, DistributionValues) => Double
  // Si rho:AgentsPolMeasure y sb:SpecificBelief
  // y d:DistributionValues ,
  // rho(sb,d) es la polarizacion de los agentes
  // de acuerdo a esa medida

  def rho(alpha: Double, beta: Double): AgentsPolMeasure = {

    (sb: SpecificBelief, dist: DistributionValues) => {
      // sb   = Vector con la creencia de cada agente
      // dist = Vector(0.0, 0.25, 0.5, 0.75, 1.0) por ejemplo

      val n = sb.length // número de agentes
      val k = dist.length // número de intervalos

      //Construir los límites de cada intervalo
      // Para cada i, calculamos el límite inferior y superior
      val limites: Vector[(Double, Double)] = Vector.tabulate(k) { i =>
        val limInf =
          if (i == 0) 0.0
          else (dist(i - 1) + dist(i)) / 2.0

        val limSup =
          if (i == k - 1) 1.0000001 // un poco más de 1 para incluir el 1.0 exacto
          else (dist(i) + dist(i + 1)) / 2.0

        (limInf, limSup)
      }
      // limites = Vector((0.0,0.125), (0.125,0.375), (0.375,0.625),
      //                  (0.625,0.875), (0.875,1.0000001))

      //Contar agentes en cada intervalo y convertir a proporción
      val frecuencias: Frequency = Vector.tabulate(k) { i =>
        val (limInf, limSup) = limites(i)
        val conteo = sb.count(creencia => creencia >= limInf && creencia < limSup)
        conteo.toDouble / n.toDouble
      }
      // frecuencias = Vector(0.5, 0.0, 0.0, 0.0, 0.5) para allExtremeBelief

      //Aplicar Comete normalizado
      val medida = normalizar(rhoCMT_Gen(alpha, beta))
      medida(frecuencias, dist)
    }
  }

  // Para implementar la función de influencia I se
  // definen los siguientes tipos de datos:
  type WeightedGraph = (Int, Int) => Double
  // recibe dos agentes i y j,
  // devuelve un número en [0,1] que representa cuánto influye
  // el agente i sobre el agente j.
  // Regla especial: I(i,i) = 1.0 siempre (cada agente se influye a sí mismo

  type SpecificWeightedGraph = (WeightedGraph, Int)
  // empaqueta la función de influencia
  // junto con el número de agentes de la red.
  // Es necesario conocer n para poder recorrer todos los agentes
  // y construir la matriz de influencia completa.

  type GenericWeightedGraph = Int => SpecificWeightedGraph
  // Genera los grafos
  // recibe el número de agentes n
  // y devuelve un SpecificWeightedGraph listo para usar.
  // Permite definir un tipo de red una sola vez y aplicarlo
  // a redes de cualquier tamaño sin repetir la lógica de influencia.

  def showWeightedGraph(swg: SpecificWeightedGraph): IndexedSeq[IndexedSeq[Double]] = {

    //Obtener la Funcion y número de agentes
    val (graf, n) = swg
    // graf = la función I(i,j)
    // n    = número de agentes

    // Para cada agente i crear una fila
    Vector.tabulate(n) { i =>

      //Para cada agente j calcular la influencia
      Vector.tabulate(n) { j =>
        graf(i, j)
      }
    }
  }

  def confBiasUpdate(b: SpecificBelief, swg: SpecificWeightedGraph): SpecificBelief = {

    val (graf, n) = swg  //desempaca

    // Para cada agente i calcular su nueva creencia
    Vector.tabulate(n) { i =>

      // Encontrar Aᵢ — agentes que influyen sobre i
      val Ai = (0 until n).filter(j => graf(j, i) > 0)

      // Calcular la suma de términos
      val suma = Ai.map { j =>
        val beta = 1.0 - math.abs(b(j) - b(i)) // similitud
        val influencia = graf(j, i) // I(j,i)
        val diferencia = b(j) - b(i) // dirección
        beta * influencia * diferencia // término completo
      }.sum

      // Nueva creencia
      b(i) + suma / Ai.length
    }
  }



}