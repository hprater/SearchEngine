rm -rf vue/dist
cd java
./mvnw clean package
cd ../vue
npm run build
cd ..
cf push