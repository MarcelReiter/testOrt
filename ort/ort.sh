REPO_CONFIG_FILE=ort/.ort.yml
RESULT_DIR="ort/results/latest"

# fail fast and pass on error codes
set -euo pipefail

# Run a custom-configured version of ort
# - with lots of memory
# - mount our local working directory to the docker /project container.
#   This allows accessing project files in docker - and syncs changes in the docker project dir to our local folder
# - with ort image from ghcr.io
# - log everything, incl. infos
# - pass the ort config file
# - pass on all other arguments
run_ort() {
  docker run \
    --memory=24g \
    -e JAVA_OPTS="-Xmx24g" \
    --mount type=bind,source="$PWD",target=/project \
    ghcr.io/oss-review-toolkit/ort \
      --info \
      -c /project/ort/config.yml \
      "$@"
}

# change to local project-dir
cd ..

# create result dir if not yet existing
mkdir -p "$RESULT_DIR"

# clean the result dear in case it's already existing
find "$RESULT_DIR" -mindepth 1 -delete

# add an orange log message
echo -e "\e[1;33m Running ORT analyzer \e[0m"

# Use our custom configured version of ort to run an analysis
# This will create a dependency-tree and write it to RESULT_DIR/analyzer-result.yml
# Make sure to also check config.yml for analyzer-configuration
run_ort analyze \
    --repository-configuration-file /project/$REPO_CONFIG_FILE \
    --input-dir /project \
    --output-dir /project/"$RESULT_DIR"

# add an orange log message
echo -e "\e[1;33m Running ORT scanner \e[0m"

# Use our custom configured version of ort to run Scancode
# This will download all dependencies and scan them for license information line-by-line
# Make sure to also check config.yml for scanner-configuration
run_ort scan \
    --package-types=PACKAGE \
    --ort-file /project/"$RESULT_DIR"/analyzer-result.yml \
    --scanners=ScanCode \
    --output-dir /project/"$RESULT_DIR" \
  || echo -e "\e[1;33m ⚠️ There were issues during the ORT scan. Still trying to run report. \e[0m"

# add an orange log message
echo -e "\e[1;33m Running ORT reporter \e[0m"

# Use our custom configured version of ort to run a report
# This will take the analyzer-result.yml created in the last step and convert it to a different format
# Make sure to also check config.yml for reporter-configuration
run_ort report \
    --repository-configuration-file /project/$REPO_CONFIG_FILE \
    --ort-file /project/"$RESULT_DIR"/scan-result.yml \
    --output-dir /project/"$RESULT_DIR" \
    --report-formats StaticHtml,WebApp
