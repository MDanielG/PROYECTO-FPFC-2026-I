
import Comete._
import Opinion._
// =============================================================================
// DATOS DE PRUEBA (definidos en el enunciado del proyecto)
// =============================================================================

val likert5: DistributionValues = Vector(0.0, 0.25, 0.5, 0.75, 1.0)

// Distribuciones de frecuencia de referencia
val pi_max    = Vector(0.5, 0.0, 0.0, 0.0, 0.5)  // Maxima polarizacion esperada
val pi_min    = Vector(0.0, 0.0, 1.0, 0.0, 0.0)  // Minima polarizacion (consenso centro)
val pi_der    = Vector(0.4, 0.0, 0.0, 0.0, 0.6)  // Sesgado a la derecha
val pi_izq    = Vector(0.6, 0.0, 0.0, 0.0, 0.4)  // Sesgado a la izquierda
val pi_int1   = Vector(0.0, 0.5, 0.0, 0.5, 0.0)
val pi_int2   = Vector(0.25, 0.0, 0.5, 0.0, 0.25)
val pi_int3   = Vector(0.25, 0.25, 0.0, 0.25, 0.25)
val pi_cons_centro = pi_min
val pi_cons_der    = Vector(0.0, 0.0, 0.0, 0.0, 1.0)
val pi_cons_izq    = Vector(1.0, 0.0, 0.0, 0.0, 0.0)


// =============================================================================
// BLOQUE 1: Pruebas de minP
// =============================================================================
// minP debe encontrar el minimo de una funcion convexa en un intervalo dado.
// Probamos con funciones cuyo minimo conocemos exactamente.

println("=" * 60)
println("PRUEBAS: minP")
println("=" * 60)

// Prueba 1.1 — Parabola x^2: minimo en 0.0
val min_cuadratica = minP(x => x * x, -1.0, 1.0, 1e-6)
println(s"[1.1] minP(x^2, -1, 1)       = $min_cuadratica  (esperado ~ 0.0)")
assert(Math.abs(min_cuadratica - 0.0) < 1e-4, "Fallo prueba 1.1")

// Prueba 1.2 — (x - 0.3)^2: minimo en 0.3
val min_desplazada = minP(x => (x - 0.3) * (x - 0.3), 0.0, 1.0, 1e-6)
println(s"[1.2] minP((x-0.3)^2, 0, 1)  = $min_desplazada  (esperado ~ 0.3)")
assert(Math.abs(min_desplazada - 0.3) < 1e-4, "Fallo prueba 1.2")

// Prueba 1.3 — Caso base: intervalo ya menor que prec → devuelve punto medio
val min_caso_base = minP(x => x * x, 0.4, 0.6, 1.0)
println(s"[1.3] minP caso base prec=1.0 = $min_caso_base  (esperado 0.5)")
assert(Math.abs(min_caso_base - 0.5) < 1e-9, "Fallo prueba 1.3")

// Prueba 1.4 — Funcion lineal convexa: minimo en el extremo izquierdo
val min_lineal = minP(x => x, 0.0, 1.0, 1e-6)
println(s"[1.4] minP(x, 0, 1)           = $min_lineal  (esperado ~ 0.0)")
assert(min_lineal < 1e-3, "Fallo prueba 1.4")

println()


// =============================================================================
// BLOQUE 2: Pruebas de rhoCMTGen
// =============================================================================
// Verificamos que los valores coincidan con los del enunciado (3 decimales).

println("=" * 60)
println("PRUEBAS: rhoCMTGen (alpha=1.2, beta=1.2)")
println("=" * 60)

val cmt1 = rhoCMT_Gen(1.2, 1.2)

// Resultados esperados segun el enunciado
val res_max   = cmt1(pi_max,         likert5)  // esperado: 0.379
val res_min   = cmt1(pi_min,         likert5)  // esperado: 0.0
val res_der   = cmt1(pi_der,         likert5)  // esperado: 0.327
val res_izq   = cmt1(pi_izq,         likert5)  // esperado: 0.327
val res_int1  = cmt1(pi_int1,        likert5)  // esperado: 0.165
val res_int2  = cmt1(pi_int2,        likert5)  // esperado: 0.165
val res_int3  = cmt1(pi_int3,        likert5)  // esperado: 0.237
val res_cc    = cmt1(pi_cons_centro, likert5)  // esperado: 0.0
val res_cd    = cmt1(pi_cons_der,    likert5)  // esperado: 0.0
val res_ci    = cmt1(pi_cons_izq,    likert5)  // esperado: 0.0

println(s"[2.1]  cmt1(pi_max)         = ${f"$res_max%.3f"}  (esperado 0.379)")
println(s"[2.2]  cmt1(pi_min)         = ${f"$res_min%.3f"}  (esperado 0.000)")
println(s"[2.3]  cmt1(pi_der)         = ${f"$res_der%.3f"}  (esperado 0.327)")
println(s"[2.4]  cmt1(pi_izq)         = ${f"$res_izq%.3f"}  (esperado 0.327)")
println(s"[2.5]  cmt1(pi_int1)        = ${f"$res_int1%.3f"}  (esperado 0.165)")
println(s"[2.6]  cmt1(pi_int2)        = ${f"$res_int2%.3f"}  (esperado 0.165)")
println(s"[2.7]  cmt1(pi_int3)        = ${f"$res_int3%.3f"}  (esperado 0.237)")
println(s"[2.8]  cmt1(pi_cons_centro) = ${f"$res_cc%.3f"}  (esperado 0.000)")
println(s"[2.9]  cmt1(pi_cons_der)    = ${f"$res_cd%.3f"}  (esperado 0.000)")
println(s"[2.10] cmt1(pi_cons_izq)    = ${f"$res_ci%.3f"}  (esperado 0.000)")

// Verificaciones con tolerancia de 0.001
assert(Math.abs(res_max  - 0.379) < 0.001, "Fallo prueba 2.1")
assert(Math.abs(res_min  - 0.0)   < 0.001, "Fallo prueba 2.2")
assert(Math.abs(res_der  - 0.327) < 0.001, "Fallo prueba 2.3")
assert(Math.abs(res_izq  - 0.327) < 0.001, "Fallo prueba 2.4")
assert(Math.abs(res_int1 - 0.165) < 0.001, "Fallo prueba 2.5")
assert(Math.abs(res_int2 - 0.165) < 0.001, "Fallo prueba 2.6")
assert(Math.abs(res_int3 - 0.237) < 0.001, "Fallo prueba 2.7")
assert(Math.abs(res_cc   - 0.0)   < 0.001, "Fallo prueba 2.8")
assert(Math.abs(res_cd   - 0.0)   < 0.001, "Fallo prueba 2.9")
assert(Math.abs(res_ci   - 0.0)   < 0.001, "Fallo prueba 2.10")

// Propiedad matematica: distribucion mas polarizada debe dar mayor valor
println()
println("[2.11] Propiedad: cmt1(pi_max) > cmt1(pi_int1)")
assert(res_max > res_int1, "Fallo: pi_max deberia polarizar mas que pi_int1")
println("       OK: la distribucion bimodal extrema polariza mas que la intermedia")

// Simetria: pi_der y pi_izq son simetricas, deben dar el mismo resultado
println("[2.12] Propiedad simetria: cmt1(pi_der) == cmt1(pi_izq)")
assert(Math.abs(res_der - res_izq) < 1e-9, "Fallo: deberian ser simetricas")
println("       OK: distribucion sesgada derecha = sesgada izquierda")

println()


// =============================================================================
// BLOQUE 3: Pruebas de normalizar
// =============================================================================

println("=" * 60)
println("PRUEBAS: normalizar")
println("=" * 60)

val cmt1_norm = normalizar(cmt1)

val norm_max  = cmt1_norm(pi_max,         likert5)  // esperado: 1.0
val norm_min  = cmt1_norm(pi_min,         likert5)  // esperado: 0.0
val norm_der  = cmt1_norm(pi_der,         likert5)  // esperado: 0.863
val norm_izq  = cmt1_norm(pi_izq,         likert5)  // esperado: 0.863
val norm_int1 = cmt1_norm(pi_int1,        likert5)  // esperado: 0.435
val norm_int2 = cmt1_norm(pi_int2,        likert5)  // esperado: 0.435
val norm_int3 = cmt1_norm(pi_int3,        likert5)  // esperado: 0.625
val norm_cc   = cmt1_norm(pi_cons_centro, likert5)  // esperado: 0.0
val norm_cd   = cmt1_norm(pi_cons_der,    likert5)  // esperado: 0.0
val norm_ci   = cmt1_norm(pi_cons_izq,    likert5)  // esperado: 0.0

println(s"[3.1]  cmt1_norm(pi_max)         = ${f"$norm_max%.3f"}  (esperado 1.000)")
println(s"[3.2]  cmt1_norm(pi_min)         = ${f"$norm_min%.3f"}  (esperado 0.000)")
println(s"[3.3]  cmt1_norm(pi_der)         = ${f"$norm_der%.3f"}  (esperado 0.863)")
println(s"[3.4]  cmt1_norm(pi_izq)         = ${f"$norm_izq%.3f"}  (esperado 0.863)")
println(s"[3.5]  cmt1_norm(pi_int1)        = ${f"$norm_int1%.3f"}  (esperado 0.435)")
println(s"[3.6]  cmt1_norm(pi_int2)        = ${f"$norm_int2%.3f"}  (esperado 0.435)")
println(s"[3.7]  cmt1_norm(pi_int3)        = ${f"$norm_int3%.3f"}  (esperado 0.625)")
println(s"[3.8]  cmt1_norm(pi_cons_centro) = ${f"$norm_cc%.3f"}  (esperado 0.000)")
println(s"[3.9]  cmt1_norm(pi_cons_der)    = ${f"$norm_cd%.3f"}  (esperado 0.000)")
println(s"[3.10] cmt1_norm(pi_cons_izq)    = ${f"$norm_ci%.3f"}  (esperado 0.000)")

assert(Math.abs(norm_max  - 1.0)   < 0.001, "Fallo prueba 3.1")
assert(Math.abs(norm_min  - 0.0)   < 0.001, "Fallo prueba 3.2")
assert(Math.abs(norm_der  - 0.863) < 0.002, "Fallo prueba 3.3")
assert(Math.abs(norm_izq  - 0.863) < 0.002, "Fallo prueba 3.4")
assert(Math.abs(norm_int1 - 0.435) < 0.002, "Fallo prueba 3.5")
assert(Math.abs(norm_int2 - 0.435) < 0.002, "Fallo prueba 3.6")
assert(Math.abs(norm_int3 - 0.625) < 0.002, "Fallo prueba 3.7")
assert(Math.abs(norm_cc   - 0.0)   < 0.002, "Fallo prueba 3.8")
assert(Math.abs(norm_cd   - 0.0)   < 0.002, "Fallo prueba 3.9")
assert(Math.abs(norm_ci   - 0.0)   < 0.002, "Fallo prueba 3.10")

// Propiedad: el peor caso normalizado siempre debe dar exactamente 1.0
println()
println("[3.11] Propiedad: normalizar(m)(peor_caso) == 1.0 siempre")
val peor_caso_frec: Frequency          = Vector(0.5, 0.0, 0.0, 0.0, 0.5)
val peor_caso: Distribution            = (peor_caso_frec, likert5)
val norm_del_peor = cmt1_norm(peor_caso)
println(s"       cmt1_norm(peor_caso) = $norm_del_peor  (esperado exactamente 1.0)")
assert(Math.abs(norm_del_peor - 1.0) < 1e-9, "Fallo: el peor caso normalizado debe ser 1.0")

// Propiedad: todo resultado normalizado esta en [0, 1]
println("[3.12] Propiedad: todos los resultados normalizados estan en [0, 1]")
val todos_norm = List(norm_max, norm_min, norm_der, norm_izq, norm_int1,
  norm_int2, norm_int3, norm_cc, norm_cd, norm_ci)
assert(todos_norm.forall(v => v >= 0.0 && v <= 1.0),
  "Fallo: algun valor normalizado esta fuera de [0, 1]")
println("       OK: todos en rango")

println()
println("======================================================")
println("TODAS LAS PRUEBAS DE COMETE PASARON EXITOSAMENTE")
println("======================================================")


// ===========================================================================
// DATOS DE PRUEBA OPINION
// ===========================================================================

val dist1: DistributionValues = Vector(0.0, 0.25, 0.50, 0.75, 1.0)
val dist2: DistributionValues = Vector(0.0, 0.2,  0.4,  0.6,  0.8, 1.0)

// Generadores de creencias
def uniformBelief(nags: Int): SpecificBelief =
  Vector.tabulate(nags)(i => (i + 1).toDouble / nags.toDouble)

def midlyBelief(nags: Int): SpecificBelief = {
  val middle = nags / 2
  Vector.tabulate(nags)(i =>
    if (i < middle) math.max(0.25 - 0.01 * (middle - i - 1), 0)
    else math.min(0.75 + 0.01 * (i - middle), 1))
}

def allExtremeBelief(nags: Int): SpecificBelief = {
  val middle = nags / 2
  Vector.tabulate(nags)(i => if (i < middle) 0.0 else 1.0)
}

def allTripleBelief(nags: Int): SpecificBelief = {
  val oneThird = nags / 3
  val twoThird = (nags / 3) * 2
  Vector.tabulate(nags)(i =>
    if (i < oneThird) 0.0
    else if (i >= twoThird) 1.0
    else 0.5)
}

def consensusBelief(b: Double)(nags: Int): SpecificBelief =
  Vector.tabulate(nags)(_ => b)

def i1(nags: Int): SpecificWeightedGraph =
  ((i: Int, j: Int) =>
    if (i == j) 1.0
    else if (i < j) 1.0 / (j - i).toDouble
    else 0.0,
    nags)

def i2(nags: Int): SpecificWeightedGraph =
  ((i: Int, j: Int) =>
    if (i == j) 1.0
    else if (i < j) (j - i).toDouble / nags.toDouble
    else (nags - (i - j)).toDouble / nags.toDouble,
    nags)
// Creencias de 100 agentes
val sb_ext    = allExtremeBelief(100)
val sb_cons   = consensusBelief(0.2)(100)
val sb_unif   = uniformBelief(100)
val sb_triple = allTripleBelief(100)
val sb_midly  = midlyBelief(100)

// Creencias de 10 agentes
val sbu_10 = uniformBelief(10)
val sbm_10 = midlyBelief(10)

// Grafos de prueba
val i1_4  = i1(4)
val i2_4  = i2(4)
val i1_10 = i1(10)

// Medidas
val rho1 = rho(1.2, 1.2)
val rho2 = rho(2.0, 1.0)

// ===========================================================================
// BLOQUE 4: Pruebas de rho
// ===========================================================================

println()
println("=" * 60)
println("PRUEBAS: rho")
println("=" * 60)

println("\n── Con dist1 ──")
println(s"[4.1]  rho1(allExtreme)  = ${f"${rho1(sb_ext,    dist1)}%.3f"}  (esperado 1.000)")
println(s"[4.2]  rho1(consensus)   = ${f"${rho1(sb_cons,   dist1)}%.3f"}  (esperado 0.000)")
println(s"[4.3]  rho1(uniform)     = ${f"${rho1(sb_unif,   dist1)}%.3f"}  (esperado 0.380)")
println(s"[4.4]  rho1(triple)      = ${f"${rho1(sb_triple, dist1)}%.3f"}  (esperado 0.617)")
println(s"[4.5]  rho1(midly)       = ${f"${rho1(sb_midly,  dist1)}%.3f"}  (esperado 0.784)")
println(s"[4.6]  rho2(allExtreme)  = ${f"${rho2(sb_ext,    dist1)}%.3f"}  (esperado 1.000)")
println(s"[4.7]  rho2(uniform)     = ${f"${rho2(sb_unif,   dist1)}%.3f"}  (esperado 0.188)")
println(s"[4.8]  rho2(triple)      = ${f"${rho2(sb_triple, dist1)}%.3f"}  (esperado 0.448)")
println(s"[4.9]  rho2(midly)       = ${f"${rho2(sb_midly,  dist1)}%.3f"}  (esperado 0.580)")

println("\n── Con dist2 ──")
println(s"[4.10] rho1(allExtreme)  = ${f"${rho1(sb_ext,    dist2)}%.3f"}  (esperado 1.000)")
println(s"[4.11] rho1(uniform)     = ${f"${rho1(sb_unif,   dist2)}%.3f"}  (esperado 0.377)")
println(s"[4.12] rho1(triple)      = ${f"${rho1(sb_triple, dist2)}%.3f"}  (esperado 0.617)")
println(s"[4.13] rho1(midly)       = ${f"${rho1(sb_midly,  dist2)}%.3f"}  (esperado 0.773)")
println(s"[4.14] rho2(uniform)     = ${f"${rho2(sb_unif,   dist2)}%.3f"}  (esperado 0.172)")
println(s"[4.15] rho2(midly)       = ${f"${rho2(sb_midly,  dist2)}%.3f"}  (esperado 0.528)")

// ===========================================================================
// BLOQUE 5: Pruebas de showWeightedGraph
// ===========================================================================

println()
println("=" * 60)
println("PRUEBAS: showWeightedGraph")
println("=" * 60)

println("\n── Grafo i1 con 4 agentes ──")
showWeightedGraph(i1_4).foreach(fila =>
  println("   " + fila.map(v => f"$v%.2f").mkString("  ")))

println("\n── Grafo i2 con 4 agentes ──")
showWeightedGraph(i2_4).foreach(fila =>
  println("   " + fila.map(v => f"$v%.2f").mkString("  ")))

// ===========================================================================
// BLOQUE 6: Pruebas de confBiasUpdate
// ===========================================================================

println()
println("=" * 60)
println("PRUEBAS: confBiasUpdate")
println("=" * 60)

val sbu_act = confBiasUpdate(sbu_10, i1_10)
val sbm_act = confBiasUpdate(sbm_10, i1_10)

println("\n── uniformBelief(10) con i1 ──")
println(s"Antes:   $sbu_10")
println(s"Despues: $sbu_act")
println(s"[6.1] rho1 antes:   ${f"${rho1(sbu_10,  dist1)}%.3f"}  (esperado 0.383)")
println(s"[6.2] rho1 despues: ${f"${rho1(sbu_act, dist1)}%.3f"}  (esperado 0.380)")

println("\n── midlyBelief(10) con i1 ──")
println(s"Antes:   $sbm_10")
println(s"Despues: $sbm_act")
println(s"[6.3] rho1 antes:   ${f"${rho1(sbm_10,  dist1)}%.3f"}  (esperado 0.435)")
println(s"[6.4] rho1 despues: ${f"${rho1(sbm_act, dist1)}%.3f"}  (esperado 0.435)")

// ===========================================================================
// BLOQUE 7: Pruebas de simulate
// ===========================================================================

println()
println("=" * 60)
println("PRUEBAS: simulate")
println("=" * 60)

println("\n── simulate uniformBelief(10), t=2 ──")
simulate(confBiasUpdate, i1_10, sbu_10, 2).zipWithIndex.foreach { case (b, t) =>
  println(s"   t=$t: rho1=${f"${rho1(b, dist1)}%.3f"}")
}
println("   (esperado: t=0→0.383, t=1→0.380, t=2→0.335)")

println("\n── simulate midlyBelief(10), t=2 ──")
simulate(confBiasUpdate, i1_10, sbm_10, 2).zipWithIndex.foreach { case (b, t) =>
  println(s"   t=$t: rho1=${f"${rho1(b, dist1)}%.3f"}")
}
println("   (esperado: t=0→0.435, t=1→0.435, t=2→0.377)")

println()
println("=" * 60)
println("TODAS LAS PRUEBAS PASARON EXITOSAMENTE")
println("=" * 60)