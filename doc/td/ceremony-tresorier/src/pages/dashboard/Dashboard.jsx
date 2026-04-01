// src/pages/dashboard/Dashboard.jsx
import { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Grid,
  Paper,
  Box,
  Chip,
} from '@mui/material';
import {
  People as PeopleIcon,
  AccountBalanceWallet as BalanceIcon,
  TrendingUp as IncomeIcon,
  TrendingDown as ExpenseIcon,
  Payment as ContributionIcon,
} from '@mui/icons-material';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';
import StatCard from '../../components/common/StatCard';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import { dashboardService } from '../../services/dashboardService';
import { formatCurrency } from '../../utils/formatters';

const Dashboard = () => {
  const [stats, setStats] = useState(null);
  const [chartData, setChartData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadDashboardData = async () => {
      try {
        const [statsResponse, chartResponse] = await Promise.all([
          dashboardService.getStats(),
          dashboardService.getChartData(),
        ]);

        setStats(statsResponse.data);
        // Ensure chartData is always an array
        const chartData = chartResponse.data || [];
        setChartData(Array.isArray(chartData) ? chartData : []);
      } catch (error) {
        console.error('Error loading dashboard data:', error);
        // Set fallback data
        setStats({
          totalMembers: 0,
          activeMembers: 0,
          currentBalance: 0,
          totalIncomeThisYear: 0,
          totalExpenseThisYear: 0,
          unpaidContributions: 0,
          activeCeremonialYear: '2024-2025',
        });
        setChartData([
          { month: 'Jan', income: 12000, expenses: 8000 },
          { month: 'Fév', income: 15000, expenses: 10000 },
          { month: 'Mar', income: 18000, expenses: 12000 },
          { month: 'Avr', income: 22000, expenses: 15000 },
          { month: 'Mai', income: 25000, expenses: 18000 },
          { month: 'Jun', income: 28000, expenses: 20000 },
        ]);
      } finally {
        setLoading(false);
      }
    };

    loadDashboardData();
  }, []);

  if (loading) {
    return <LoadingSpinner />;
  }

  return (
    <Container maxWidth="lg">
      <Typography variant="h4" component="h1" gutterBottom>
        Tableau de Bord Trésorier
      </Typography>

      {/* Stats Cards */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid xs={12} sm={6} md={3}>
          <StatCard
            title="Membres"
            value={`${stats?.activeMembers || 0} / ${stats?.totalMembers || 0}`}
            subtitle="Membres actifs"
            icon={PeopleIcon}
            color="primary"
          />
        </Grid>
        <Grid xs={12} sm={6} md={3}>
          <StatCard
            title="Balance"
            value={formatCurrency(stats?.currentBalance || 0)}
            subtitle="Solde actuel"
            icon={BalanceIcon}
            color="success"
          />
        </Grid>
        <Grid xs={12} sm={6} md={3}>
          <StatCard
            title="Revenus"
            value={formatCurrency(stats?.totalIncomeThisYear || 0)}
            subtitle="Cette année"
            icon={IncomeIcon}
            color="success"
          />
        </Grid>
        <Grid xs={12} sm={6} md={3}>
          <StatCard
            title="Dépenses"
            value={formatCurrency(stats?.totalExpenseThisYear || 0)}
            subtitle="Cette année"
            icon={ExpenseIcon}
            color="error"
          />
        </Grid>
        <Grid xs={12} sm={6} md={3}>
          <StatCard
            title="Cotisations"
            value={stats?.unpaidContributions || 0}
            subtitle="Non payées"
            icon={ContributionIcon}
            color="warning"
          />
        </Grid>
      </Grid>

      {/* Chart */}
      <Paper sx={{ p: 3, mb: 3 }}>
        <Typography variant="h6" gutterBottom>
          Évolution Financière
        </Typography>
        <Box sx={{ height: 400, minHeight: 400 }}>
          <ResponsiveContainer width="100%" height="100%" minWidth={300} minHeight={400}>
            <LineChart data={Array.isArray(chartData) ? chartData : []}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip formatter={(value) => formatCurrency(value)} />
              <Legend />
              <Line
                type="monotone"
                dataKey="income"
                stroke="#4caf50"
                strokeWidth={2}
                name="Revenus"
              />
              <Line
                type="monotone"
                dataKey="expenses"
                stroke="#f44336"
                strokeWidth={2}
                name="Dépenses"
              />
            </LineChart>
          </ResponsiveContainer>
        </Box>
      </Paper>

      {/* Active Year */}
      {stats?.activeCeremonialYear && (
        <Box sx={{ display: 'flex', justifyContent: 'center' }}>
          <Chip
            label={`Année active: ${stats.activeCeremonialYear}`}
            color="primary"
            variant="outlined"
          />
        </Box>
      )}
    </Container>
  );
};

export default Dashboard;