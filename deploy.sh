cd java
./mvnw clean package

cd ../vue
rm -rf dist
npm run build

cd ..
cf push