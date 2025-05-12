# Data Distribution Service (DDS)

A lightweight publish-subscribe system that enables distributed communication between nodes using IP multicast.

## Overview

This project implements a distributed data service with a publish-subscribe pattern using Java UDP multicast sockets.

## Architecture

The system uses IP multicast for efficient one-to-many communication. Each topic is automatically assigned a unique multicast group address and port number.

## Components

### Node

[`Node`](src/node/Node.java) serves as a container for publishers and subscribers. It provides:
- Auto-allocation of multicast addresses for topics
- Creation of publishers and subscribers
- Socket management

### Publisher

[`Publisher`](src/publisher/Publisher.java) sends messages to specific topics with features including:
- Configurable publishing rate
- Continuous or one-time publishing
- Thread-safe operation

### Subscriber

[`Subscriber`](src/subscriber/Subscriber.java) receives messages from topics with:
- Automatic topic subscription
- Background message reception
- Thread-safe operation

### Registry (Optional)

[`Registry`](src/registry/Registry.java) provides an alternative approach for topic management using a centralized registry pattern.

## Usage

### Building the Project

```bash
cd src
./make.sh
```

### Running a Publisher

```bash
java Main
```

### Running a Subscriber

```bash
java Main2
```

### Example Code

Creating a publisher:

```java
Node node = new Node("publisher-node");
Publisher pub = node.createPublisher("topic1", 1000); // 1000ms publishing rate
pub.startPublishing("Hello World");
```

Creating a subscriber:

```java
Node node = new Node("subscriber-node");
Subscriber sub = node.createSubscriber("topic1");
// Messages will be printed to console automatically
```