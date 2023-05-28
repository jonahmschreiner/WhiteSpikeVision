What is this?
This project is a finished partial implementation of a digital artificial general intelligence that I drew up between August 2022 and January 2023. The architecture was rationalized by thinking of an instant of human consciousness as a function (where the environment was the input and an action taken was the output), and life itself as a repetition of that function (which I called an LLF, looped-life-function).

Why a partial implementation? 
I orginally implemented the entire model at once and as you've probably guessed, it had a never-ending series of bugs. To resolve this, I decided to redo it as independent packages to decouple them and make the debugging process easier.
This package provides everything needed for an AGI to be able to take advantage of vision sources (in testing I used display screenshots, but because it was built to identify objects through pixel color, any image/video input should work)

Why are the architectural diagrams not included?
Don't want to give away the entire thing in case I come back and finish this in the future lol

How do I use it?
As a java library for your own application by using: Env env = new Env();

What is an Env?
The containing variable that holds the complete information about an instance of reality (or at least as many properties as you include).
This usually means all Senses (objects) that have been found, the Blobs (collection of pixels) that make up those Senses, how those senses have changed since the previous instance, etc.

What are the dependencies?
Should only be MYSQL and the Java connector for it (might not even need those as pretty sure I decoupled the database as well)
note: I'm writing this a while after stopping work on it (needed to start earning money again in order to support myself) so there might be some things I forgot about

What platforms does it work on?
Should work on both linux and windows operating systems (tested it on ubuntu and windows x64 machines)

How can I contribute?
Reach out to me at jonahmschreiner@gmail.com and we can talk :)
