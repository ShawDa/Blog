package main

import (
	"fmt"
	"strconv"
)

func main() {
	intChannel := make(chan int, 100)
	finishChannel := make(chan bool, 1)

	go writeInt(intChannel)
	go readInt(intChannel, finishChannel)

	for {
		_, ok := <-finishChannel
		if !ok {
			break
		}
	}
}

func writeInt(intChannel chan int) {
	for i := 0; i < 100; i++ {
		intChannel <- i
		fmt.Println("writeInt" + strconv.Itoa(i))
	}
	close(intChannel)
}

func readInt(intChannel chan int, finishChannel chan bool) {
	for {
		v, ok := <-intChannel
		if !ok {
			break
		}
		fmt.Println("readInt" + strconv.Itoa(v))
	}

	finishChannel <- true
	close(finishChannel)
}
