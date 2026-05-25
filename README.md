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

Segun lo aprendido en clase las clases no deberian saber como funciona cada skin pro como vemos estamos cometiendo ese error y para rematar ahora miremos la clase de GamePanel que se esta encargando de hacer tres if para saber de que tipo es el cuadrado y basado en eso dibujarlo:

![image.alt]()

## 2.Nueva Moneda
