# Use Node.js for building the Angular app
FROM node:18 as build

WORKDIR /app
COPY package*.json ./
RUN npm install
RUN npm install --legacy-peer-deps
COPY . .
RUN npm run build --prod

# Use Nginx for serving the Angular app
FROM nginx:1.25
COPY --from=build /app/dist/your-angular-app /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
