package main

import "fmt"

func main() {
	fmt.Println(person{"Bob", 20})
	fmt.Println(person{name: "Alice", age: 30})
	fmt.Println(person{name: "Fred"})
	fmt.Println(&person{name: "Ann", age: 40})
	fmt.Println(newPerson("Join", 50))

	s := person{"Sean", 60}
	fmt.Println(s.name, s.age)

	s.age = 62
	fmt.Println(s.age)

	sp := &s
	fmt.Println(sp.age)

	sp.age = 66
	fmt.Println(sp.age)
}

type person struct {
	name string
	age  int
}

func newPerson(name string, age int) *person {
	p := person{name: name}
	p.age = age
	return &p
}

/*
{Bob 20}
{Alice 30}
{Fred 0}
&{Ann 40}
&{Join 50}
Sean 60
62
62
66
*/
