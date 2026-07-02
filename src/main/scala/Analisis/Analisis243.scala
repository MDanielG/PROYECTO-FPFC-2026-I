package Analisis

import Comete._
import Opinion._
import Benchmark._

object Analisis243 {

  def main(args: Array[String]): Unit = {

    val likert5: DistributionValues = Vector(0.0, 0.25, 0.5, 0.75, 1.0)

    val sbms: Seq[SpecificBelief] = for {
      n    <- 2 until 16
      nags  = math.pow(2, n).toInt
    } yield midlyBelief(nags)

    // ========================================================================
    //  (A) rho vs rhoPar   -> compararMedidasPol
    //  rho es O(n) con trabajo trivial por elemento; se prueba con redes MUCHO
    //  más grandes (hasta 2^20 ~ 1 millón de agentes) para ver si aparece un
    //  crossover donde rhoPar empiece a ser más rápida.
    // ========================================================================
    val sbmsMedida: Seq[SpecificBelief] = for {
      n    <- 2 to 20
      nags  = math.pow(2, n).toInt
    } yield midlyBelief(nags)
    val polSec = rho(1.2, 1.2)
    val polPar = rhoPar(1.2, 1.2)
    val cmpMed = compararMedidasPol(sbmsMedida, likert5, polSec, polPar)

    println("=" * 82)
    println("(A) rho (secuencial) vs rhoPar (paralelo)")
    println("=" * 82)
    println(f"${"n agentes"}%10s | ${"polSec"}%8s | ${"polPar"}%8s | ${"t_seq(ms)"}%12s | ${"t_par(ms)"}%12s | ${"acel"}%7s")
    println("-" * 82)
    cmpMed.foreach { case (n, p1, p2, t1, t2, acel) =>
      println(f"$n%10d | $p1%8.3f | $p2%8.3f | ${t1.value}%12.4f | ${t2.value}%12.4f | $acel%7.2f")
    }

    // ========================================================================
    //  (B) confBiasUpdate vs confBiasUpdatePar   -> compararFuncionesAct
    // ========================================================================
    val sbmsAct = sbms.take(10)                 // 2^2 .. 2^11
    val grafoGrande = i2(sbmsAct.last.length)   // i2 del mayor de esos tamaños
    val cmpAct = compararFuncionesAct(sbmsAct, grafoGrande, confBiasUpdate, confBiasUpdatePar)

    println()
    println("=" * 82)
    println("(B) confBiasUpdate (secuencial) vs confBiasUpdatePar (paralelo)")
    println("=" * 82)
    println(f"${"n agentes"}%10s | ${"t_seq(ms)"}%14s | ${"t_par(ms)"}%14s | ${"acel"}%8s")
    println("-" * 82)
    cmpAct.foreach { case (n, t1, t2, acel) =>
      println(f"$n%10d | ${t1.value}%14.4f | ${t2.value}%14.4f | $acel%8.2f")
    }

    // ========================================================================
    //  (C) Simulación completa: simulate con update secuencial vs paralelo.
    // ========================================================================
    val pasos = 10
    println()
    println("=" * 82)
    println(s"(C) Simulación completa ($pasos pasos): update secuencial vs paralelo")
    println("=" * 82)
    println(f"${"n agentes"}%10s | ${"t_seq(ms)"}%14s | ${"t_par(ms)"}%14s | ${"acel"}%8s")
    println("-" * 82)
    val cmpSim = for {
      b   <- sbmsAct
      swg  = i2(b.length)
      t1   = tiempoDe(simulate(confBiasUpdate,    swg, b, pasos))
      t2   = tiempoDe(simulate(confBiasUpdatePar, swg, b, pasos))
    } yield (b.length, t1.value, t2.value, t1.value / t2.value)
    cmpSim.foreach { case (n, t1, t2, acel) =>
      println(f"$n%10d | $t1%14.4f | $t2%14.4f | $acel%8.2f")
    }

    // ========================================================================
    //  (D) GRÁFICAS: evolución de la polarización en el tiempo (simEvolucion).
    // ========================================================================
    val nGraf = 100
    val opinionesIniciales: Seq[SpecificBelief] = Seq(
      allExtremeBelief(nGraf),      // totalmente polarizada (mitad en 0, mitad en 1)
      consensusBelief(0.2)(nGraf),  // consenso (todos en 0.2)
      uniformBelief(nGraf),         // opiniones uniformemente repartidas
      allTripleBelief(nGraf),       // tres polos (0, 0.5, 1)
      midlyBelief(nGraf)            // medianamente polarizada
    )
    simEvolucion(opinionesIniciales, i2(nGraf), 20, polSec, confBiasUpdate, likert5,
                 "Evolucion de la polarizacion")

    println()
    println("Gráfica generada: revisa el archivo 'simulEvol.html' en la raíz del proyecto.")

  }
}
