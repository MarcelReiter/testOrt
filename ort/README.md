### Prerequisites
You need a working **docker** installation.
And since my company doesn't allow docker ui i need to use colima instead.

To install this on MacOS just run:
```
brew install docker
brew install colima 
```

Running an ort-scan using the ort-docker image is pretty memory intensive.
Colima needs to be configured to allow such heavy tasks like this:

```
colima stop
colima start --cpu 8 --memory 24
```

### Running ORT
There's a nice little script which automates the ort process.
Just run the `ort.sh` script found in the `ort` directory using your terminal or IDE of choice.
The results of the ort-scan can be found in the `ort/results` directory.

### Configuring ORT
The script automatically picks up the ORT configuration files: 
- `ort/config.yml`

The script and the configuration files are documented extensively, so that they should be easy to alter.

### Scanner
The scanner step of ORT uses the Scancode Toolkit.

#### Caching Scanner results
A fresh Scancode-scan can take quite a while, depending on your network speed & available processing power.
Therefore, each dependencies scan-results are cached & automatically reused, cutting scanning-time to minutes.
The caching directory is mounted to `project/ort/cache`, which allows easy access from both docker container and your local machine.
