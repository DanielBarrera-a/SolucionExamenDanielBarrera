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

![image.alt]()

![image.alt]()

Para la solcuion decidi implementar dos cosas, 1.Una interfaz () y 2.Una clase abstracta que son las encargada de describir el comportamiento de los diferetes cuadrados y se separo de la clase player

Nueva interfaz SkinBehavior:

![image.alt]()

Nueva clase abstracta AbstractSkinBehavior:

![image.alt]()

Se hace de esta manera para que las skins tengan que usar de las interfaz todos los metodos del juego y con la clase abstracta cada quien reescribe los metodos segun su compartamiento

Luego se crearon las clases para cada tipo de Skin (RedSkinBehavior, BlueSkinBehavior y GreenSkinBehavior)  y de paso estamos dejando el codigo listo para extenderlo con otra nueva skin solo creariamos nueva SkinBehavior






## 2.Nueva Moneda
