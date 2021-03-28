# petra #

[![version pre-alpha](https://img.shields.io/badge/version-pre--alpha-orange?style=shield)](https://github.com/cognitionbox/petra)
[![Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-brightgreen?style=shield)](https://www.apache.org/licenses/LICENSE-2.0) [![<CognitionBox>](https://circleci.com/gh/cognitionbox/petra.svg?style=shield)](https://circleci.com/gh/cognitionbox) [![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=shield)](https://github.com/cognitionbox/petra)

## Overview ##

### What is Petra? ###

* Programmable Workflows
* Seamless Object & State Oriented Programming
* Easier Parallel Programming
* Easier Verification & Validation
* Java 8 Embedded DSL

Petra is a concise and expressive object-oriented 
general purpose workflow programming language which aims to make 
state-oriented, parallel and distributed programs easier to code and verify.                         
                    
The aim of Petra is to provide a modern programming paradigm, 
a concise language and tooling to meet today's complex software needs. 
Petra has verification in mind at it has been designed from the ground up to 
produce safe, regularized, concise, object-orientated, parallel, distributed systems, 
which are easy to reason about and to minimize and detected 
programming errors, whilst being a practical tool for industry.
 
Petra's reference implementation is a general purpose embedded Java 8 DSL 
(Domain Specific Language), with built in automatic software verification features.
The embedded DSL has been designed in a way that makes it feel like a native language.
Most embedded DSLs in Java use method chaining, however Petra uses a function sequence pattern
for better readability. Future versions will support the builder pattern (method chaining) 
in addition to the function sequence pattern as it is useful for dynamically / programmatically building up statements. 
This would be useful in an AI system that needs to write and verify it's own code.

Petra programs are easy to reason about and they are constructed in a declarative manor.
Petra has parallel and distributed programming built into its core and a program can be executed in 
sequential, parallel or distributed modes, whilst maintaining consistent semantics.

Currently distributed implementation is experimental and is likely to be unstable.

### What is this repository for? ###

This repo contains the following projects: 

#### petra-core ####
Java source code for the reference implementation of Petra.
 
#### petra-hazelcast-jet (experimental) ####
Java implementation for the Hazelcast JET implementation of the Petra components
factory ```IPetraComponentsFactory```. This enables Petra to be executed using
distributed objects using Hazelcast's in-memory data grid technology and 
Jet streaming technology. Jet can process large distributed data structures with
lower latency.

#### petra-unit ####
Contains source for Petra's step test system which makes it easy to unit test petra components.

### How do I get set up? ###

Simply clone the repo and navigate to the project's root directory then do
```mvn clean install``` to build and install the project jars to your local maven repo.

This will build the projects in the order as defined by parent pom file (see below)

```
<modules>
    <module>petra-core</module>
    <module>petra-hazelcast-jet</module>
    <module>petra-unit</module>
</modules>
```

Then you can include the ```petra-core``` and/or
 ```petra-unit``` dependencies in your projects (see below).

```
<dependency>
	<groupId>io.cognitionbox.petra</groupId>
	<artifactId>petra-core</artifactId>
	<version>pre-alpha</version>
</dependency>
```

```
<dependency>
	<groupId>io.cognitionbox.petra</groupId>
	<artifactId>petra-unit</artifactId>
	<version>pre-alpha</version>
</dependency>
```

Please look at the tests within the examples package to start understanding Petra.
```petra-core``` includes support for sequential and parallel execution modes, 
however Petra has been constructed in a way that will allow for distributed mode implementations.

### Petra's Programming Paradigm ###

Petra's two kinds of computation steps: PGraph, PEdge (see below).
PGraphs can contain other PGraphs or PEdges. 
PEdges contain Java code which mutates state.
These steps have 'pre/post' conditions or 'kases' 
which provide a contract for describing how the state is mutated.
This allow complex system to be composed of a hierarchy of steps, 
whilst being able to mutate state in a safe.

Petra makes it easier to do parallel programming by removing the need to hand code
concurrency primitives like threads, locks and fork/joins, etc.

Petra also ensures the parallel programming is carried out in a safe way in order to prevent
common errors such as deadlocks, race-conditions and data-races. 

Petra has a form of transactional memory which allows it to rollback to "good" states,
before retrying computations, which makes its easier to understand the possible state transitions.

### Use Cases ###
Petra is well suited to back-end processing, including but not limited to,
critical business processes, infrastructure orchestration, scientific workflows, 
modelling and execution of business processes, AI, machine learning, 
smart contract execution and blockchain applications.

### Petra Components, Operations & Features ###

#### Components ####
##### PComputer #####
This is used to start a Petra system and is invoked from the Java main method.
```java
public class PetraExample {
    public static void main(String[] args){

        // logs progression of tokens in places
        PComputer.getConfig().enableStatesLogging();
        
        // The line below kicks off a Petra graph named AtoAGraph which takes A as input and
        // returns A as output.

        A output = new PComputer<A>().eval(new AtoAGraph(),new A(""));
        System.out.println("OUTPUT: "+output.value);

    }
}
```
##### PGraph #####
A PComputer must take as input a PGraph and the input to start with.
A PGraph has a pre-condition and a post-condition, or a number of kases, 
each of which have both a pre-condition and a post-condition.
The PGraph only starts if its pre-condition is met.
A PGraph can also have multiple steps, where each step can be another PGraph or a PEdge.
When a another PGraph or PEdge becomes a step of a higher-level PGraph,
they all become children of that PGraph, and thus this PGraph is the parent of these children.
```java
public class AtoAGraph extends PGraph<A> {
    {
       type(A.class);
       pre(a->a.value.equals(""));
       step(a->a, new AtoA());
       post(a->a.value.equals("hello world."));
    }
}
```

##### PEdge #####
PEdges are the finest unit of computation in the Petra system, in that
they cannot be composed of any other Petra components.

```java
public class AtoA extends PEdge<A> {
    {
       type(A.class);
       pre(a->a.value.equals(""));
       func(a->{
           a.value = "hello world.";
       });
       post(a->a.value.equals("hello world."));
    }
}
```

##### Collections #####

Petra's PCollection's are designed to have the same behaviour across, 
sequential, parallel and distributed modes, without having to change the code.
So far there are 3 PCollection types: ```PList``` , ```PSet``` and ```PMap```.

You can also use Java collections or other library collections but without the guarantee 
semantics will stay consistent over all the runtime modes (sequential, parallel and distributed).

#### Operations ####
##### Pre-conditions #####
##### Post-conditions #####
##### step #####
A PGraph can have any number of steps which are either themselves PGraphs or PEdges.
```java
public class SeqGraph extends PGraph<X> {
            {
                type(X.class);
                pre(x->x.isAB());
                step(x->x, new SeqEdge2());
                step(x->x, new SeqEdge1());
                post(x->x.isC());
            }
        }
```

##### func #####
Each PEdge must have a "func" which contains 
Java code which performs a computation.

#### Example Features ####

##### Dynamic Step Parallelism #####
If there are multiple instances of a class in a place and there is a step which can
consume this type of instance, then Petra will dynamically create a step for each instance,
thus it scales with the data.

##### Transactional Memory #####
Petra ensures computations either complete successfully, or rollback to a state before the
computation started. This means it becomes easier to reason about a complex system as we always know
what sort of states the system can be in, rather than worrying about partial states. 

##### Automatic Retries #####
Petra is designed to roll back the changes made by a PEdge if an error occurs which causes the 
computation to terminate early or if the post-condition is not met.
This means PEdge computations will be automatically retried 
as their start state will be present again. 
This means developers no longer have to write explicit retry code.

##### Construction Checks #####

Petra includes several construction checks which are on by default.
These checks prevent a Petra system from running if one of the checks fail.
The checks enforce the semantics of the Petra system to provide safety guarantees.
If there are multiple issues that prevent start-up all of these are logged to the console.

##### JVM language support #####

###### Java 8 ######
Petra has been written in Java 8 as an embedded DSL and therefore
Java 8 support is native to Petra.

### Future Work ###

#### Distributed Deployment Architecture ####

Petra aims to be deployed against any Hazelcast cluster hosted on-site or on in the cloud or directly on Hazelcast Cloud for convenience.
Petra worker nodes can run on any Java 8 environment. When Petra worker nodes start they compete to become the master node.
There can only be one master node, all other nodes will be workers. 
The master node owns the root level Petra iteration loop. If the master goes down one of the other worker nodes
will try to become the master in order to own and execute the root iteration loop.
When an Petra application starts the root iteration loop causes tasks to be added to Petra's task ring buffer.
All nodes are able to process tasks from the ring buffer. During processing of a task more tasks can be added to the ring buffer.
Tasks are never removed from the ring buffer but they are marked as complete once successfully completed.

Below is a simple diagram showing the distributed deployment architecture. 

![Alt text](https://g.gravizo.com/svg?digraph%20PetraArchitecture%20{rankdir=LR;%22Petra%20worker%20node%201%20(Master)%22-%3E%22Hazelcast%20IMDG%20/%20JET%20cluster%22%22Petra%20worker%20node%202%22-%3E%22Hazelcast%20IMDG%20/%20JET%20cluster%22%22Petra%20worker%20node%203%22-%3E%22Hazelcast%20IMDG%20/%20JET%20cluster%22%22Petra%20worker%20node%20n%20...%22-%3E%22Hazelcast%20IMDG%20/%20JET%20cluster%22})
     

### Who do I talk to? ###

The idea of Petra was originally thought up by Aran Hakki in 2011.
Since then he has been researching and developing the system.

Aran originally read MEng Systems Engineering at Warwick University 
and has been a software engineer for over 10 years.
He started a PhD in Computer Science at the University of Southampton 
in May 2020 and the main research topics are those surrounding the Petra programming system.

If you are interested in this project and believe you could benefit 
from this technology please get in touch.

* Aran Hakki
* a.hakki@soton.ac.uk
* aran.hakki@gmail.com
* +44(0)7399472347
