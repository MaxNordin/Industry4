import scala.collection.mutable.ArrayBuffer
import Array._
import astar._
object Main {
	def main(args: Array[String]) {
		var leftPlates1 = new Array[Byte](16)
		var rightPlates1 = new Array[Byte](16)
		var middle1: Array[Byte] = Array(0,0,0,0)
		var leftRobot1: Byte = 0
		
		var rightRobot1: Byte = 0
		var leftPlates2 = new Array[Byte](16)
		var rightPlates2 = new Array[Byte](16)
		var middle2: Array[Byte] = Array(0,0,0,0)
		var leftRobot2: Byte = 0
		var rightRobot2: Byte = 0
		
		//leftPlates1 = Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)
		//rightPlates1 = Array(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
		//middle1 = Array(2,2,2,2)
		
		//leftPlates2 = Array(2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0)
		//rightPlates2 = Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)
		//middle2 = Array(0,0,0,0)
		
		var nBlocks = args(0).toInt
		var endTime : Long = 0
		var startTime : Long = 0
		var loops = 1000
		var time : Double = 0
		var ops : Double = 0
		var parops : Double = 0
		
		var desiredState : BlockState = null
		var start : BlockState = null
		var moves : Array[Array[Move]] = null
		
		for(i <- 1 to loops){
			leftPlates1 = new Array[Byte](16)
			rightPlates1 = new Array[Byte](16)
			leftPlates2 = new Array[Byte](16)
			rightPlates2 = new Array[Byte](16)
			placeBlocks(nBlocks, leftPlates1, rightPlates1)
			placeBlocks(nBlocks, leftPlates2, rightPlates2)

			desiredState = new BlockState(leftPlates2, rightPlates2, middle2, leftRobot2, rightRobot2, 0, null, null)
			start = new BlockState(leftPlates1, rightPlates1, middle1, leftRobot1, rightRobot1, 0, desiredState, ArrayBuffer[Move]())
			
			startTime = System.nanoTime
			moves = Astar.solver(start)
			endTime = System.nanoTime
			
			time += (endTime - startTime) / 1e6d
			parops += moves(0).length
			for (j <- 0 to moves(0).length-1){
				if (moves(0)(j) != null) ops += 1
			}
			for (j <- 0 to moves(1).length-1){
				if (moves(1)(j) != null) ops += 1
			}
			println(i)
		}
		
		time = time/loops
		ops = ops/loops
		parops = parops/loops
		
		println("Time: " + time + " ms")
		println("Operations: " + ops)
		println("Parallel operations: " + parops)
		
		/*
		var s = ""
		println(s)
		BlockState.printState(start)
		BlockState.printState(desiredState)
		for (i <- 0 to moves(0).length-1) {
			if (moves(0)(i) != null && moves(0)(i).position < 9) s += moves(0)(i).toString + "   "
			else if (moves(0)(i) != null) s += moves(0)(i).toString + "  "
			else s += "         "
			if (moves(1)(i) != null) s += moves(1)(i).toString
			s +=  "\n"
		} 
		println(s)
		*/
	}
	
	def placeBlocks(nBlocks: Int, leftPlate: Array[Byte], rightPlate: Array[Byte]){
		var color = 1
		var n = 0
		var r = 0
		val random = scala.util.Random
		var left = true
		while (color < 5){
			while (n < nBlocks) {
				r = random.nextInt(16)
				left = random.nextInt(2) == 0
				if (left && leftPlate(r) == 0) {
					leftPlate(r) = color.toByte
					n += 1
				}
				else if (rightPlate(r) == 0) {
					rightPlate(r) = color.toByte
					n += 1
				}
			}
			n = 0
			color += 1 
		}
	}
}