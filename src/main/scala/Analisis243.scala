// ============================================================================
//  PUNTO 2.4.3 — Producir datos para la evaluación comparativa
//                (versiones secuencial vs concurrente)
//
//  Usa org.scalameter (vía Benchmark.tiempoDe / compararMedidasPol /
//  compararFuncionesAct) y expresiones for para generar las tablas de tiempos:
//    (A) rho            vs rhoPar
//    (B) confBiasUpdate vs confBiasUpdatePar
//    (C) simulación completa (simulate con update secuencial vs paralelo)
//
//  Cómo correr en IntelliJ:
//    Abre este archivo -> botón verde ▶ junto a "object Analisis243" -> Run.
//    La salida (tablas) aparece en el panel Run. Cópialas al informe (sección 6.4).
// ============================================================================

import Comete._
import Opinion._
import Benchmark._   // tiempoDe, compararMedidasPol, compararFuncionesAct, midlyBelief, i2, ...

object Analisis243 {

  def main(args: Array[String]): Unit = {

    val likert5: DistributionValues = Vector(0.0, 0.25, 0.5, 0.75, 1.0)

    // ========================================================================
    //  GENERACIÓN DE ENTRADAS con una expresión 'for':
    //  creencias "medianamente polarizadas" de tamaños 2^2, 2^3, ..., 2^15.
    // ========================================================================
    val sbms: Seq[SpecificBelief] = for {
      n    <- 2 until 16
      nags  = math.pow(2, n).toInt
    } yield midlyBelief(nags)

    // ========================================================================
    //  (A) rho vs rhoPar   -> compararMedidasPol
    //  Devuelve séxtuplas: (n, p1, p2, t1, t2, aceleracion)
    // ========================================================================
    val polSec = rho(1.2, 1.2)
    val polPar = rhoPar(1.2, 1.2)
    val cmpMed = compararMedidasPol(sbms, likert5, polSec, polPar)

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
    //  La actualización es O(n^2); usamos las creencias hasta 2^11.
    //  Un único grafo grande (i2 del mayor tamaño), como en el enunciado.
    //  (Válido porque confBiasUpdate usa b.length.)
    //  Devuelve cuádruplas: (n, t1, t2, aceleracion)
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
    //  Medimos el tiempo total de 'pasos' unidades de tiempo con tiempoDe.
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

    println()
    println("Listo. Copia las tres tablas al informe (seccion 6.4).")
  }
}
