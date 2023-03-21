# ReinforcementLearning-Mapping
This Repo is a java program that uses reinforcement learning to allow an agent to map out a terrain to a goal.

To test clone the repository and compile the ReinforcementLearning.java file.
![Compile](https://user-images.githubusercontent.com/92120183/226712824-d02b758d-1f22-4c06-9b6e-3069034d9d10.PNG)

Once it is compiled you can run the program.
![Run](https://user-images.githubusercontent.com/92120183/226712917-2e88998c-72c4-4aaf-862b-a934ef142fd8.PNG)

There are multiple command arguments that you can use.

These are the following flags:

-f <FILENAME>: Reads the environment from the file named <FILENAME> (specified as a String).

-a <DOUBLE>: Specifies the (initial) learning rate (step size) alpha ∈ [0, 1]; default is 0.9.

-e <DOUBLE>: Specifies the (initial) policy randomness value epsilon ∈ [0, 1]; default is 0.9.

-g <DOUBLE>: Specifies the discount rate gamma ∈ [0, 1] to use during learning; default is 0.9.

-na <INTEGER>: Specifies the value N alpha which controls the decay of the learning rate (step size) alpha; default is 1000.

-ne <INTEGER>: Specifies the value N epsilon which controls the decay of the random action threshold epsilon; default is 200.

-p <DOUBLE>: Specifies the action success probability p ∈ [0, 1]; default is 0.8.

-q: Toggles the use of Q-Learning with off-policy updates (instead of SARSA with on-policy updates, which is the default).

-T <INTEGER>: Specifies the number of learning episodes (trials) to perform; default is 10000.

-u: Toggles the use of Unicode characters in printing; disabled by default.

-v <INTEGER>: Specifies a verbosity level, indicating how much output the program should produce; default is 1.

-P: Specifies the play mode where the user can input directions to move the agent around the map and test their score against the learning algorithm.

The order of the arguments does not matter.

The verbosity will allow you to see the steps in the rewards as well as final outcomes of each of the possible points on the map.

![SARSA-RunVerbosity3](https://user-images.githubusercontent.com/92120183/226712968-14bf60c3-e2ec-4cc4-b307-f220fb57c92e.PNG)

