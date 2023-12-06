cd java
./mvnw clean package

rm -rf vue/dist
cd ../vue
npm run build

cd ..
cf push