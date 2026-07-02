================================================================================
 PROYECTO DE CURSO - SIMULADOR DE POLARIZACION EN REDES
 Fundamentos de Programacion Funcional y Concurrente
 Escuela de Ingenieria de Sistemas y Computacion - Universidad del Valle
 Docente: Juan Francisco Diaz Frias
================================================================================

AUTORES
--------------------------------------------------------------------------------
 Luis Santiago Arenas Hincapie - 2435300
 Ana Sofia Cantillo Bolaños - 2610112
 Daniel Andres Micolta Gongora - 2422033
 Jairo Andrés Tegüé Gómez - 2416227

1. DESCRIPCION
--------------------------------------------------------------------------------
 Este proyecto implementa un simulador de polarizacion en redes sociales en
 lenguaje Scala, bajo el paradigma de programacion funcional y concurrente.
 Se modela una poblacion de agentes con creencias en [0,1] sobre una
 proposicion, se mide su polarizacion (medida "comete") y se observa su
 evolucion en el tiempo bajo la dinamica de sesgo de confirmacion. Ademas de
 las versiones secuenciales, se implementan versiones paralelas de las funciones
 mas costosas y se realiza la evaluacion comparativa de desempeno.


2. CONTENIDO DE LA ENTREGA
--------------------------------------------------------------------------------
 Informe.pdf
     Informe del proyecto: estructuras de datos, tecnicas funcionales,
     argumentacion de correccion, analisis de paralelizacion y tablas de
     tiempos con sus conclusiones.

 Readme.txt

 src/main/scala/Comete/package.scala
     Paquete Comete. Contiene la medida de polarizacion base:
        - minP        (2.1.1) : minimo de una funcion convexa (busqueda ternaria).
        - rhoCMT_Gen  (2.1.1) : medida de polarizacion "comete" parametrizada.
        - normalizar  (2.1.1) : normaliza una medida al intervalo [0,1].

 src/main/scala/Opinion/package.scala
     Paquete Opinion. Modela la red de agentes y su evolucion:
        - rho                (2.2.1) : medida de polarizacion de una red.
        - showWeightedGraph  (2.3.1) : matriz del grafo de influencia.
        - confBiasUpdate     (2.3.2) : actualizacion por sesgo de confirmacion.
        - simulate           (2.3.3) : evolucion de la creencia en el tiempo.
        - rhoPar             (2.4.1) : version paralela de rho (paralelismo de datos).
        - confBiasUpdatePar  (2.4.2) : version paralela de confBiasUpdate
                                       (paralelismo de datos y de tareas).

 src/main/scala/Analisis/Analisis243.scala   (COMPLEMENTO - punto 2.4.3)
     Objeto ejecutable que genera los DATOS de la evaluacion comparativa
     (secuencial vs paralelo) usando org.scalameter y expresiones for, y
     produce la grafica de evolucion de la polarizacion (simEvolucion).
     Sus resultados se presentan tabulados en el Informe (seccion 6).

 src/test/scala/pruebas.sc   (CASOS DE PRUEBA DEL GRUPO)
     Worksheet con las pruebas diseñadas por el equipo para verificar la
     corrección de las funciones de los paquetes Comete y Opinion.

3. REQUISITOS
--------------------------------------------------------------------------------
 - Scala 2.13.10
 - JDK 17  (IMPORTANTE: no usar JDK 21 o superior; Scala 2.13 no los soporta
            y falla la construccion del compiler-bridge)
 - sbt 1.9.x
 - IntelliJ IDEA con el plugin de Scala
 - Dependencias (declaradas en build.sbt):
        com.storm-enroute %% scalameter-core          % 0.21
        org.scala-lang.modules %% scala-parallel-collections
        org.plotly-scala %% plotly-render              % 0.8.1  (para las graficas)
        org.scalameta %% munit                         % Test


4. COMO EJECUTAR
--------------------------------------------------------------------------------
 4.1. Armar el proyecto
      Coloque los paquetes Comete y Opinion en un proyecto sbt/IntelliJ junto
      con los paquetes common y Benchmark. Abra el proyecto en IntelliJ y espere
      a que sbt importe las dependencias (JDK 17 seleccionado como SDK).

 4.2. Ejecutar las pruebas
      Abra el worksheet de pruebas (por ejemplo src/test/scala/pruebas.sc) y
      ejecutelo con el boton Run del worksheet. Debe imprimir los resultados y
      terminar con "TODAS LAS PRUEBAS PASARON EXITOSAMENTE".

 4.3. Generar los datos de la evaluacion comparativa (punto 2.4.3)
      Ejecute el objeto Analisis.Analisis243.
      En la consola apareceran tres tablas de tiempos:
          (A) rho vs rhoPar
          (B) confBiasUpdate vs confBiasUpdatePar
          (C) simulacion completa (secuencial vs paralela)
      Ademas se genera el archivo "simulEvol.html" (en la raiz del proyecto) con
      la grafica de evolucion de la polarizacion; abralo en un navegador.

5. NOTAS
--------------------------------------------------------------------------------
 - Todas las funciones son funciones puras con estructuras inmutables (Vector).
 - Las versiones paralelas producen el mismo resultado que las secuenciales;
   solo cambian los tiempos de ejecucion.
 - El detalle de la argumentacion de correccion y el analisis de desempeno se
   encuentra en el Informe.pdf.
================================================================================
