If you are interested in developing and building the project please read the following the instructions carefully.

## Version control

To get sources of the project, please execute:

```sh
git clone https://github.com/holunda-io/camunda-bpm-correlate.git
cd camunda-bpm-correlate
```

We are using gitflow in our git SCM for naming branches. That means that you should start from `develop` branch,
create a `feature/<name>` out of it and once it is completed create a pull request containing
it. Please squash your commits before submitting and use semantic commit messages, if possible.

## Project Build

Perform the following steps to get a development setup up and running.

```sh
./mvnw clean install
```

## Integration Tests

By default, the build command will ignore the run of `failsafe` Maven plugin executing the integration tests
(usual JUnit tests with class names ending with ITest). In order to run integration tests, please
call from your command line:

```sh
./mvnw -Pitest
```

## Project build modes and profiles

### Documentation

We are using MkDocs for generation of a static site documentation and rely on markdown as much as possible.

!!! note

    If you want to develop your docs in 'live' mode, run `mkdocs serve` and access
    the http://localhost:8000/ from your browser.

For creation of documentation, please run:

#### Generation of JavaDoc and Sources

By default, the sources and javadoc API documentation are not generated from the source code. To enable this:

```sh
./mvnw clean install -Prelease -Dgpg.skip=true
```

### Continuous Integration

GitHub Actions are building all branches on commit hook (for codecov).
In addition, a GitHub Actions are used to build PRs and all branches.

### Publish a new release

We use gitflow plugin to handle versioning and branch manipulations between `develop` and `master`. Technically,
every push to the `master` branch triggers the execution of the GH actions job producing a release and publishing
it into Maven Central. To do it correctly (with correct versions) please run:

```sh
./mvnw gitflow:release-start
```

Acknowledge the proposed version (or change if needed) and then run:

```sh
./mvnw gitflow:release-finish
```

### Milestone / Release Management

After the publication of the new release, it is time to tell the users that you produced a new version.
The `GitHub release` is produced by using the GitHub feature `Close Milestone`. A special GitHub action
is preparing the release notes as a draft. Then click on `Publish Release` to make it public.
This will trigger some GitHub internal notifications and people subscribed to notification about the library
will get notified.

#### What modules get deployed to repository

Every Maven module is enabled by default. If you want to change this, please provide the property

```xml
<maven.deploy.skip>true</maven.deploy.skip>
```

inside the corresponding `pom.xml`. Currently, all `examples` are _EXCLUDED_ from publication into Maven Central.
