# Vercel Deployment Guide for WeatherPro Frontend

This guide will help you deploy the WeatherPro frontend to Vercel.

## Prerequisites

1. A Vercel account (free tier works fine): https://vercel.com/signup
2. Your backend deployed somewhere accessible (Railway, Render, Heroku, etc.)
3. Backend URL should be HTTPS for production

## Step 1: Prepare Your Backend

Before deploying the frontend, ensure your backend is:

1. **Deployed and accessible** via HTTPS
2. **CORS configured** to allow your Vercel domain

Update `application.properties` in your backend:
```properties
# Add your Vercel domain to CORS allowed origins
cors.allowed-origins=https://your-app.vercel.app,http://localhost:5173
```

## Step 2: Deploy to Vercel

### Option A: Deploy via Vercel CLI (Recommended)

1. **Install Vercel CLI:**
```bash
npm install -g vercel
```

2. **Navigate to frontend directory:**
```bash
cd weatherpro-frontend
```

3. **Login to Vercel:**
```bash
vercel login
```

4. **Deploy:**
```bash
vercel
```

5. **Set environment variable when prompted:**
```
VITE_API_BASE_URL=https://your-backend-url.com/api
```

6. **Deploy to production:**
```bash
vercel --prod
```

### Option B: Deploy via Vercel Dashboard (Easiest)

1. **Push your code to GitHub** (already done ✓)

2. **Go to Vercel Dashboard:**
   - Visit: https://vercel.com/new
   - Click "Import Project"
   - Select your GitHub repository: `VivekChoudhary77/WeatherProApp`

3. **Configure Project:**
   - **Framework Preset:** Vite
   - **Root Directory:** `weatherpro-frontend`
   - **Build Command:** `npm run build`
   - **Output Directory:** `dist`
   - **Install Command:** `npm install`

4. **Add Environment Variable:**
   - Click "Environment Variables"
   - Add variable:
     - **Name:** `VITE_API_BASE_URL`
     - **Value:** `https://your-backend-url.com/api`
     - **Environment:** Production, Preview, Development

5. **Click "Deploy"**

## Step 3: Update Backend CORS

Once deployed, you'll get a URL like: `https://weather-pro-app.vercel.app`

Update your backend's CORS configuration:

```properties
cors.allowed-origins=https://weather-pro-app.vercel.app,http://localhost:5173
```

Redeploy your backend for the changes to take effect.

## Step 4: Test Your Deployment

1. Visit your Vercel URL: `https://weather-pro-app.vercel.app`
2. Try searching for a location
3. Check if weather data loads correctly
4. Test all features:
   - ✓ Location search (city, ZIP, coordinates, landmarks)
   - ✓ Current weather display
   - ✓ 5-day forecast
   - ✓ YouTube recommendations
   - ✓ Export functionality (JSON, XML, CSV, PDF)
   - ✓ CRUD operations in weather history

## Troubleshooting

### Issue: "Network Error" or "CORS Error"

**Solution:** Check backend CORS settings and ensure:
- Backend allows your Vercel domain
- Backend is accessible via HTTPS
- Environment variable `VITE_API_BASE_URL` is correct

### Issue: "404 on Refresh"

**Solution:** Vercel should handle this with `vercel.json` rewrites. If not:
- Check that `vercel.json` exists in `weatherpro-frontend/`
- Verify the rewrites configuration

### Issue: Environment Variable Not Working

**Solution:**
- Ensure variable name starts with `VITE_`
- Redeploy after adding environment variables
- Check Vercel dashboard → Project Settings → Environment Variables

## Updating Your Deployment

### Via CLI:
```bash
cd weatherpro-frontend
vercel --prod
```

### Via GitHub:
- Push changes to your `main` branch
- Vercel will automatically deploy

## Custom Domain (Optional)

1. Go to Vercel Dashboard → Your Project → Settings → Domains
2. Add your custom domain
3. Follow Vercel's DNS configuration instructions

## Environment Variables Reference

| Variable | Example | Required |
|----------|---------|----------|
| `VITE_API_BASE_URL` | `https://api.example.com/api` | Yes |

## Production Checklist

- [ ] Backend deployed and accessible via HTTPS
- [ ] Backend CORS configured with Vercel URL
- [ ] Environment variable `VITE_API_BASE_URL` set in Vercel
- [ ] All API keys configured in backend
- [ ] Database connected and accessible
- [ ] Frontend deployed successfully
- [ ] All features tested in production
- [ ] Custom domain configured (optional)

## Vercel Features

Your app will automatically get:
- ✅ **HTTPS** (SSL certificate)
- ✅ **CDN** (Global edge network)
- ✅ **Automatic deployments** (on git push)
- ✅ **Preview deployments** (for pull requests)
- ✅ **Analytics** (optional)

## Cost

Vercel Free Tier includes:
- 100 GB bandwidth/month
- 100 deployments/day
- Preview deployments
- Custom domains
- HTTPS

This is more than enough for a portfolio/demo project!

## Support

If you encounter issues:
- Vercel Docs: https://vercel.com/docs
- Vercel Support: https://vercel.com/support
- Check build logs in Vercel dashboard

---

**Your frontend is now ready for deployment! 🚀**

