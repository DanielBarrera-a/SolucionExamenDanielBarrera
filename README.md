# Solucion del Examen
![image.alt]()
## 1.Refactorizacion 
1.Para esta parte seleccione de la lsita de candidatos a refactorizar el 3. que es El comportamiento de un personaje

-¿Porque decidi este comportamiento?

Lo que pasa es que en el proyecto soy consiente de que tenemos un problema y es que la clase Player esta violando los principios SOLID aprendidos, La S porque player solo deberia tener las responsabilidades del jugador pero dentro de esta clase se estan tambien las reglas de cada Skin, ejemplo esta misma clase es la que se encarga de la logica del escudo del cuadrado verde, la velocidad del cuadrado azul y pues esto ya de por si con la primera violava el principio solid S, aqui los ejemplos de lo que estoy diciendo:

Como podemos ver aca la clase player se esta encargando de mirar si el golpe lo recivio el cuadrado verde y se encarga de restarle el escudo:
![image.alt](https://github.com/DanielBarrera-a/SolucionExamenDanielBarrera/blob/fff1b47b2dd246576e70eec06d35b046b0899de9/ErrorEnPlayer.png)

En este caso player se enta encargando de mirar si es el cuadrado azul y como sabe que es el azul pues le dice que la velocidad del cuadrado azul es de 2 osea el doble:
![image.alt](https://github.com/DanielBarrera-a/SolucionExamenDanielBarrera/blob/fff1b47b2dd246576e70eec06d35b046b0899de9/VelocidadAzul.png)

Segun lo aprendido en clase las clases no deberian saber como funciona cada skin pro como vemos estamos cometiendo ese error y para rematar ahora miremos la clase de GamePanel que se esta encargando de hacer tres if para saber de que tipo es el cuadrado, adicionalmente GamePanel se esta dedicando tambien a dibujar los cuadrados preguntando su tipo con condicionales:

![image.alt](https://github.com/DanielBarrera-a/SolucionExamenDanielBarrera/blob/eb82e78c006961e3bdc380630b630779560d96c7/ErroEnGamePanel.png)

¿Como se viola O?

Se esta violando el principio Solid O porque si ejemplo en el "futuro" se quisiera extender mas el proyecto y agregar una nueva skin con velocidad 3 se tendria que ir a la clase Player y en el metodo getSpeed agregar un nuevo if que podria ser ( if (active == Skin.YELLOW) return 3; ) y ahi estamos violando lo que dice el principio solid de que debemos estar abiertos para extender pero cerrados para modificar y agregar un if dentro de una clse es modificar y para terminar de agregar ese nuevo cuadrado de velocidad 3 nos tocaria modificar los metodos de applyEnemyhit y resetShield y tambien toca modificar GamePanel

-¿Como se soluciono?

En la ultima clase de tema el profe nos hablo de los patrones de diseño y para solucionar este problema decidi aplicar el patron Plantilla y polimorfismo que son los dos patrones de reutilizacion:

Foto de la presentacion de moodle:

![image.alt](https://github.com/DanielBarrera-a/SolucionExamenDanielBarrera/blob/10f0aa1142a10d706d29ace224c5294bf2bfd858/patron.png)

¿Como se encuentran los test antes del refactor?

![image.alt](https://github.com/DanielBarrera-a/SolucionExamenDanielBarrera/blob/30709b10b48213a51f426e98d31723ca659709ee/antesp1.png)

![image.alt](https://github.com/DanielBarrera-a/SolucionExamenDanielBarrera/blob/30709b10b48213a51f426e98d31723ca659709ee/antesp2.png)

![image.alt](https://github.com/DanielBarrera-a/SolucionExamenDanielBarrera/blob/30709b10b48213a51f426e98d31723ca659709ee/antesc.png)

Para la solcuion decidi implementar dos cosas, 1.Una interfaz (SkinBehavior) y 2.Una clase abstracta (AbstractSkinBehavior) que son las encargada de describir el comportamiento de los diferetes cuadrados y se separo de la clase player

Nueva interfaz SkinBehavior:

![image.alt](https://github.com/DanielBarrera-a/SolucionExamenDanielBarrera/blob/30709b10b48213a51f426e98d31723ca659709ee/nuevaInterfaz.png)

Nueva clase abstracta AbstractSkinBehavior:

![image.alt](https://github.com/DanielBarrera-a/SolucionExamenDanielBarrera/blob/68376d4df97cdd5541fecf08a672b17eba2ced13/abstracantes.png)

Se hace de esta manera para que las skins tengan que usar de las interfaz todos los metodos del juego y con la clase abstracta cada quien reescribe los metodos segun su compartamiento

Luego se crearon las clases para cada tipo de Skin (RedSkinBehavior, BlueSkinBehavior y GreenSkinBehavior)  y de paso estamos dejando el codigo listo para extenderlo con otra nueva skin solo creariamos nueva SkinBehavior

Se crea la clase SkinBehaviorFactory, ya teniamos otras factorys en el codigo y esta se implemento por la misma razon de no darle la responsabilidad de crear los objetos y no romper el principio O

Adicionalmente se modifico toda la clase player para que se use las clases behavior que a su vez heredan e implementan de la interfaz y la clase abstracta y solo se modifico el metodo de drawPlayer GamePanel para quitar loq ue haciua con ifs y implementar SkinBehavior 


¿Como quedaron las pruebas despues del refactor?

![image.alt](https://github.com/DanielBarrera-a/SolucionExamenDanielBarrera/blob/c6a9537fb3e3d1b7008c34d254d46931e2c5a7e9/PruebasDespues.png)

Como podemos ver todos los test pasaron despues del refactor

## 2.Nueva Moneda

Para extender la moneda de buena manera, cabe recalcar que en el parcial la embarre porque rompi la O de Solid y me puse a modificar las clases, lo cual no debi hacer, ahora siguiendo la misma logica del punto 1 de usar las clases de comportamiento (Behavior) extendi esta nueva moneda

Pasos que se llevaron para adicionar la nueva moneda:

1. Agregar un nuevo Enum a los que ya teniamos:

![image.alt](https://github.com/DanielBarrera-a/SolucionExamenDanielBarrera/blob/c6a9537fb3e3d1b7008c34d254d46931e2c5a7e9/enum.png)

2. Se creo el ImmunutySkinBehavior:

Como lo dijimos arriba del documento si llegaba el momento de extender la clase abstracta y la interfaz nos hiban a ser de mucha ayuda a la horra de extender por que aqui la estamos usando:

![image.alt](https://github.com/DanielBarrera-a/SolucionExamenDanielBarrera/blob/ae97d7479f8584709b7424890568eefad33a2ea9/inmunity.png)

NOTA:Soy concinete que de aqui en adelante estoy violando la O de Solid pero esque agregar nuevas monedas requiere tambien de un refactor y ya no me da el tiempo

3.Se modifico SkinBehaviorFactory ya que manejamos casos porque hora de leer el juego ocurre que en el momento exacto en que el jugador pise una la moneda, para saber que reglas de velocidad y color debe aplicarle al jugador ahora que es inmune:

![image.alt](https://github.com/DanielBarrera-a/SolucionExamenDanielBarrera/blob/c6a9537fb3e3d1b7008c34d254d46931e2c5a7e9/modificacion.png)

4. Se creo la clase PulseCoin con su logica y se modifico el coinFactory para agregar el nuevo caso, adicionalmente se modificaron los metodos tickTime, moveEnemies y checkCollisions

5. 

## Comandos para correr el juego





