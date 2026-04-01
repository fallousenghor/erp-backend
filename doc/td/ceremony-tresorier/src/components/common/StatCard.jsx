// src/components/common/StatCard.jsx
import { Card, CardContent, Typography, Box } from '@mui/material';

const StatCard = ({ title, value, subtitle, icon: Icon, color = 'primary' }) => {
  return (
    <Card sx={{ minWidth: 200, height: '100%' }}>
      <CardContent>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
          {Icon && <Icon color={color} sx={{ mr: 1 }} />}
          <Typography variant="h6" component="div">
            {title}
          </Typography>
        </Box>
        <Typography variant="h4" component="div" sx={{ mb: 1 }}>
          {value}
        </Typography>
        {subtitle && (
          <Typography variant="body2" color="text.secondary">
            {subtitle}
          </Typography>
        )}
      </CardContent>
    </Card>
  );
};

export default StatCard;