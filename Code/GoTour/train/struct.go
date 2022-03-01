package main

import "fmt"

func main() {
	var extendMonster extendMonster
	extendMonster.name = "guy"
	extendMonster.age = 3
	extendMonster.int = 100
	extendMonster.n = 1000
	fmt.Println(extendMonster) // {{guy 3} 100 1000}
}

type monster struct {
	name string
	age  int
}

type extendMonster struct {
	monster
	int
	n int
}
