
FROM node:18 AS build



#RUN apt-get update && apt-get install -y npm

WORKDIR /app

COPY package*.json ./
#RUN npm install -g npm@11.0.0

RUN npm config set legacy-peer-deps true

RUN npm install -g @angular/cli@17



# Copy the rest of the application source code
COPY . .

# Build the Angular application
RUN npm run build --prod

# Use the official Nginx image for serving the application
FROM nginx:alpine

# Copy the built Angular application from the build stage to Nginx's default directory
COPY --from=build /app/dist/your-angular-app /usr/share/nginx/html

# Expose port 80 to be accessible outside the container
EXPOSE 80

# Start Nginx to serve the application
CMD ["nginx", "-g", "daemon off;"]

