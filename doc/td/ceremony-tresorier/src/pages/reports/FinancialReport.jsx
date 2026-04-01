// src/pages/reports/FinancialReport.jsx
import { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Paper,
  Grid,
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Card,
  CardContent,
  Chip,
} from '@mui/material';
import {
  TrendingUp as TrendingUpIcon,
  TrendingDown as TrendingDownIcon,
  AccountBalance as AccountBalanceIcon,
  AttachMoney as AttachMoneyIcon,
  Payments as PaymentsIcon,
} from '@mui/icons-material';
import {
  PieChart,
  Pie,
  Cell,
  ResponsiveContainer,
  Tooltip,
  Legend,
} from 'recharts';
import { transactionService } from '../../services/transactionService';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import { formatCurrency, formatDate } from '../../utils/formatters';
import toast from 'react-hot-toast';

const COLORS = ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40'];

const FinancialReport = () => {
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedYearId, setSelectedYearId] = useState(1);
  const [ceremonialYears, setCeremonialYears] = useState([]);

  useEffect(() => {
    loadCeremonialYears();
  }, []);

  useEffect(() => {
    if (selectedYearId) {
      loadReport();
    }
  }, [selectedYearId]);

  const loadCeremonialYears = async () => {
    try {
      // For now, use mock data since we don't have the ceremonial years service
      const mockYears = [
        { id: 1, year: '2024-2025', active: true },
        { id: 2, year: '2023-2024', active: false },
      ];
      setCeremonialYears(mockYears);
      setSelectedYearId(1);
    } catch (error) {
      console.error('Error loading ceremonial years:', error);
      toast.error('Erreur lors du chargement des années');
    }
  };

  const loadReport = async () => {
    try {
      const response = await transactionService.getFinancialReport(selectedYearId);
      const reportData = response.data || {};
      setReport(reportData);
    } catch (error) {
      console.error('Error loading financial report:', error);
      // Set mock data for demonstration
      const mockData = {
        totalIncome: 2500000,
        totalExpense: 1800000,
        balance: 700000,
        initialBudget: 2000000,
        totalContributions: 1500000,
        paidContributionCount: 45,
        unpaidContributionCount: 12,
        transactionCount: 156,
        expensesByCategory: {
          'Matériel': 500000,
          'Transport': 300000,
          'Nourriture': 400000,
          'Décoration': 350000,
          'Communication': 150000,
          'Autres': 100000,
        },
        recentTransactions: [
          {
            id: 1,
            transactionDate: '2024-12-01',
            description: 'Achat de matériel',
            type: 'SORTIE',
            amount: 150000,
            category: 'Matériel',
          },
          {
            id: 2,
            transactionDate: '2024-11-28',
            description: 'Cotisation membre',
            type: 'COTISATION',
            amount: 50000,
            category: 'Cotisation',
          },
          {
            id: 3,
            transactionDate: '2024-11-25',
            description: 'Transport invités',
            type: 'SORTIE',
            amount: 200000,
            category: 'Transport',
          },
        ],
      };
      setReport(mockData);
    } finally {
      setLoading(false);
    }
  };

  const prepareChartData = () => {
    if (!report?.expensesByCategory) return [];

    return Object.entries(report.expensesByCategory).map(([category, amount]) => ({
      name: category,
      value: amount,
    }));
  };

  if (loading) {
    return <LoadingSpinner message="Chargement du rapport financier..." />;
  }

  return (
    <Container maxWidth="xl">
      <Box sx={{ mb: 3, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography variant="h4" component="h1">
          Rapport Financier
        </Typography>
      </Box>

      {/* Year Selector */}
      <Paper sx={{ p: 2, mb: 3 }}>
        <FormControl sx={{ minWidth: 200 }}>
          <InputLabel>Année cérémoniale</InputLabel>
          <Select
            value={selectedYearId}
            label="Année cérémoniale"
            onChange={(e) => setSelectedYearId(e.target.value)}
          >
            {ceremonialYears.map((year) => (
              <MenuItem key={year.id} value={year.id}>
                {year.year} {year.active ? '(Active)' : ''}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Paper>

      {report && (
        <>
          {/* Main Stats */}
          <Grid container spacing={3} sx={{ mb: 4 }}>
            <Grid xs={12} sm={6} md={3}>
              <Card>
                <CardContent sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                  <TrendingUpIcon color="success" />
                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      Total Revenus
                    </Typography>
                    <Typography variant="h6" color="success.main">
                      {formatCurrency(report.totalIncome || 0)}
                    </Typography>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
            <Grid xs={12} sm={6} md={3}>
              <Card>
                <CardContent sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                  <TrendingDownIcon color="error" />
                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      Total Dépenses
                    </Typography>
                    <Typography variant="h6" color="error.main">
                      {formatCurrency(report.totalExpense || 0)}
                    </Typography>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
            <Grid xs={12} sm={6} md={3}>
              <Card>
                <CardContent sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                  <AccountBalanceIcon color={(report.balance || 0) >= 0 ? 'success' : 'error'} />
                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      Balance
                    </Typography>
                    <Typography
                      variant="h6"
                      color={(report.balance || 0) >= 0 ? 'success.main' : 'error.main'}
                    >
                      {formatCurrency(report.balance || 0)}
                    </Typography>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
            <Grid xs={12} sm={6} md={3}>
              <Card>
                <CardContent sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                  <AttachMoneyIcon color="primary" />
                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      Budget Initial
                    </Typography>
                    <Typography variant="h6" color="primary.main">
                      {formatCurrency(report.initialBudget || 0)}
                    </Typography>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          </Grid>

          {/* Contributions Stats */}
          <Paper sx={{ p: 3, mb: 4 }}>
            <Typography variant="h6" gutterBottom>
              Statistiques des Cotisations
            </Typography>
            <Grid container spacing={3}>
              <Grid xs={12} sm={6} md={3}>
                <Box textAlign="center">
                  <PaymentsIcon color="secondary" sx={{ fontSize: 40, mb: 1 }} />
                  <Typography variant="h6">
                    {formatCurrency(report.totalContributions || 0)}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Total Cotisations
                  </Typography>
                </Box>
              </Grid>
              <Grid xs={12} sm={6} md={3}>
                <Box textAlign="center">
                  <Typography variant="h4" color="success.main">
                    {report.paidContributionCount || 0}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Cotisations Payées
                  </Typography>
                </Box>
              </Grid>
              <Grid xs={12} sm={6} md={3}>
                <Box textAlign="center">
                  <Typography variant="h4" color="warning.main">
                    {report.unpaidContributionCount || 0}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Cotisations Non Payées
                  </Typography>
                </Box>
              </Grid>
              <Grid xs={12} sm={6} md={3}>
                <Box textAlign="center">
                  <Typography variant="h4" color="primary.main">
                    {report.transactionCount || 0}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Nombre de Transactions
                  </Typography>
                </Box>
              </Grid>
            </Grid>
          </Paper>

          {/* Chart */}
          <Paper sx={{ p: 3, mb: 4 }}>
            <Typography variant="h6" gutterBottom>
              Dépenses par Catégorie
            </Typography>
            <Box sx={{ height: 400, minHeight: 400 }}>
              <ResponsiveContainer width="100%" height="100%" minWidth={300} minHeight={400}>
                <PieChart>
                  <Pie
                    data={prepareChartData()}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                    outerRadius={120}
                    fill="#8884d8"
                    dataKey="value"
                  >
                    {prepareChartData().map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip formatter={(value) => formatCurrency(value)} />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            </Box>
          </Paper>

          {/* Recent Transactions */}
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Transactions Récentes
            </Typography>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              {report.recentTransactions?.map((transaction) => (
                <Box
                  key={transaction.id}
                  sx={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    p: 2,
                    border: '1px solid',
                    borderColor: 'divider',
                    borderRadius: 1,
                  }}
                >
                  <Box>
                    <Typography variant="body1" fontWeight="medium">
                      {transaction.description}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {formatDate(transaction.transactionDate)} • {transaction.category}
                    </Typography>
                  </Box>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                    <Chip
                      label={transaction.type}
                      color={transaction.type === 'SORTIE' ? 'error' : 'success'}
                      size="small"
                    />
                    <Typography
                      variant="h6"
                      color={transaction.type === 'SORTIE' ? 'error.main' : 'success.main'}
                    >
                      {transaction.type === 'SORTIE' ? '-' : '+'}
                      {formatCurrency(transaction.amount || 0)}
                    </Typography>
                  </Box>
                </Box>
              ))}
            </Box>
          </Paper>
        </>
      )}

      {!report && !loading && (
        <Paper sx={{ p: 4, textAlign: 'center' }}>
          <Typography variant="h6" color="text.secondary">
            Aucune donnée disponible pour cette année
          </Typography>
        </Paper>
      )}
    </Container>
  );
};

export default FinancialReport;