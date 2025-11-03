# Render.com Deployment Guide for WeatherPro Backend

This guide will help you deploy your Spring Boot backend to Render.com for **FREE** using Docker.

Reference: [Free Hosting Bliss: Deploying Your Spring Boot App on Render](https://medium.com/spring-boot/free-hosting-bliss-deploying-your-spring-boot-app-on-render-d0ebd9713b9d)

## Prerequisites

1. A Render.com account (free): https://render.com/
2. Docker installed on your system: https://docs.docker.com/get-docker/
3. A Docker Hub account (free): https://hub.docker.com/

## Step-by-Step Deployment

### Step 1: Build the Spring Boot Application

Navigate to the backend directory and build the JAR file:

```bash
cd weatherpro-backend
./mvnw clean package -DskipTests
```

This will create a JAR file: `target/weatherpro-backend-1.0.0.jar`

### Step 2: Verify Dockerfile

The `Dockerfile` is already created in the `weatherpro-backend` directory. It:
- Uses Eclipse Temurin JDK 21 Alpine (lightweight)
- Copies the JAR file
- Exposes port 8080
- Runs the Spring Boot application

### Step 3: Build the Docker Image

Build the Docker image locally:

```bash
cd weatherpro-backend
docker build -t weatherpro-backend .
```

### Step 4: Login to Docker Hub

```bash
docker login
```

Enter your Docker Hub username and password when prompted.

### Step 5: Tag the Docker Image

Replace `YOUR_DOCKERHUB_USERNAME` with your actual Docker Hub username:

```bash
docker tag weatherpro-backend YOUR_DOCKERHUB_USERNAME/weatherpro-backend:latest
```

Example:
```bash
docker tag weatherpro-backend vivekchoudhary77/weatherpro-backend:latest
```

### Step 6: Push to Docker Hub

```bash
docker push YOUR_DOCKERHUB_USERNAME/weatherpro-backend:latest
```

Example:
```bash
docker push vivekchoudhary77/weatherpro-backend:latest
```

**Alternative: Using Docker Desktop**
- Open Docker Desktop
- Go to Images → Find `weatherpro-backend`
- Click the "Push to Hub" button

### Step 7: Deploy on Render.com

1. **Go to Render Dashboard**
   - Visit: https://dashboard.render.com/
   - Click **"New +"** → Select **"Web Service"**

2. **Deploy Existing Image**
   - Select **"Deploy an existing image from a registry"**
   - Click **"Next"**

3. **Enter Docker Image URL**
   - Go to your Docker Hub repository
   - Copy the pull command (e.g., `docker pull vivekchoudhary77/weatherpro-backend:latest`)
   - Or simply enter: `YOUR_DOCKERHUB_USERNAME/weatherpro-backend:latest`
   - Click **"Next"**

4. **Configure Web Service**
   - **Name**: `weatherpro-backend` (or your preferred name)
   - **Region**: Choose closest to you (e.g., `Ohio (US East)`)
   - **Instance Type**: Select **"Free"**
   - **Port**: `8080` (Spring Boot default)

5. **Add Environment Variables**
   
   Click **"Advanced"** → **"Add Environment Variable"** and add:

   ```
   # Database (Supabase)
   SPRING_DATASOURCE_URL=jdbc:postgresql://aws-1-us-east-1.pooler.supabase.com:5432/postgres?user=postgres.rkmaunthlhdaqedtujxn&password=YOUR_PASSWORD&sslmode=require
   
   # API Keys
   OPENWEATHER_API_KEY=your_openweather_api_key
   YOUTUBE_API_KEY=your_youtube_api_key
   GOOGLE_MAPS_API_KEY=your_google_maps_api_key
   
   # CORS (Update after deploying frontend)
   CORS_ORIGINS=https://your-frontend.vercel.app,http://localhost:5173
   ```

   **Important Environment Variables:**
   - `SPRING_DATASOURCE_URL` - Your Supabase database connection string
   - `OPENWEATHER_API_KEY` - OpenWeatherMap API key
   - `YOUTUBE_API_KEY` - YouTube Data API key
   - `GOOGLE_MAPS_API_KEY` - Google Maps API key
   - `CORS_ORIGINS` - Allowed frontend origins

6. **Create Web Service**
   - Click **"Create Web Service"**
   - Render will pull the Docker image and deploy it
   - Wait for deployment to complete (2-3 minutes)

7. **Get Your Backend URL**
   - Once deployed, you'll get a URL like: `https://weatherpro-backend.onrender.com`
   - This is your backend API URL!

## Step 8: Update Frontend Configuration

Once your backend is deployed:

1. **Update Frontend Environment Variable**
   - Go to Vercel → Your Project → Settings → Environment Variables
   - Update `VITE_API_BASE_URL` to: `https://weatherpro-backend.onrender.com/api`
   - Redeploy frontend

2. **Update CORS in Render**
   - Go to Render → Your Service → Environment
   - Update `CORS_ORIGINS` to include your Vercel URL
   - Example: `https://weather-pro-app.vercel.app,http://localhost:5173`
   - Save and let Render redeploy

## Important Notes

### ⚠️ Free Tier Limitations

- **Spin-down on inactivity**: Free instances sleep after 15 minutes of inactivity
- **Cold start**: Takes 30-50 seconds to wake up when accessed
- **750 hours/month**: Shared across all free services
- **No persistent disk**: Use external database (Supabase)

### 💡 Performance Tips

1. **Keep it warm**: Use a service like [UptimeRobot](https://uptimerobot.com/) to ping your backend every 10 minutes
2. **Optimize startup**: Minimize dependencies and use lazy initialization
3. **Use caching**: Implement Redis caching if needed

### 🔒 Security Best Practices

1. **Never commit sensitive data**:
   - API keys
   - Database passwords
   - Connection strings

2. **Use environment variables** for all sensitive configuration

3. **Enable HTTPS only** (Render provides this by default)

4. **Validate all inputs** (already implemented in the app)

## Testing Your Deployment

Once deployed, test your backend:

```bash
# Health check
curl https://weatherpro-backend.onrender.com/api/actuator/health

# Get weather history
curl https://weatherpro-backend.onrender.com/api/weather

# Test with your frontend
# Update frontend VITE_API_BASE_URL and test all features
```

## Troubleshooting

### Issue: "Failed to pull image"
**Solution:** 
- Verify Docker image exists on Docker Hub
- Check image name is correct
- Ensure image is public (not private)

### Issue: "Application failed to start"
**Solution:**
- Check logs in Render dashboard
- Verify all environment variables are set
- Ensure database is accessible (Supabase project is active)

### Issue: "Database connection timeout"
**Solution:**
- Verify Supabase project is not paused
- Check connection string is correct
- Ensure SSL mode is enabled (`sslmode=require`)

### Issue: "CORS errors"
**Solution:**
- Update `CORS_ORIGINS` in Render environment variables
- Include both your Vercel URL and localhost
- Redeploy after changes

## Monitoring

**View Logs:**
- Render Dashboard → Your Service → Logs
- See real-time application logs

**View Metrics:**
- Render Dashboard → Your Service → Metrics
- Monitor CPU, Memory, and Response times

## Updating Your Deployment

When you make changes to your backend:

```bash
# 1. Build new version
cd weatherpro-backend
./mvnw clean package -DskipTests

# 2. Build new Docker image
docker build -t weatherpro-backend .

# 3. Tag with version
docker tag weatherpro-backend YOUR_DOCKERHUB_USERNAME/weatherpro-backend:v1.1

# 4. Push to Docker Hub
docker push YOUR_DOCKERHUB_USERNAME/weatherpro-backend:v1.1

# 5. Update in Render
# Go to Render → Service → Settings → Image Path
# Update to: YOUR_DOCKERHUB_USERNAME/weatherpro-backend:v1.1
# Or use :latest tag and manually trigger redeploy
```

## Alternative: GitHub Integration

Instead of using Docker Hub, you can:
1. Push Dockerfile to GitHub
2. Let Render build directly from GitHub
3. Auto-deploy on git push

**Setup:**
- Render Dashboard → New → Web Service
- Connect GitHub repository
- Select branch
- Render will auto-detect Dockerfile

## Cost

**Free Tier Includes:**
- 750 hours/month compute time
- Automatic SSL certificates
- Global CDN
- 100 GB bandwidth/month

Perfect for portfolio projects and demos!

## Production Considerations

For production workloads, consider:
- **Paid plans** ($7/month starter) - No spin-down
- **Database backups** - Enable in Supabase
- **Monitoring** - Set up error tracking (Sentry)
- **Scaling** - Upgrade instance type as needed

---

**Your backend is now live on Render! 🚀**

Backend URL: `https://weatherpro-backend.onrender.com/api`

Next: Update your frontend's `VITE_API_BASE_URL` and deploy to Vercel!

