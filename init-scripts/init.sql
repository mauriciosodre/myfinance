-- Verificar e criar o banco de dados (execute isso conectado ao postgres principal)
DO $$
BEGIN
    -- Verificar se o banco de dados existe
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'myfinance') THEN
        -- Criar o banco de dados se não existir
        EXECUTE 'CREATE DATABASE myfinance';
        RAISE NOTICE 'Database myfinance created.';
ELSE
        RAISE NOTICE 'Database myfinance already exists.';
END IF;
END
$$;

-- Conecte-se ao banco de dados myfinance para executar os comandos abaixo

-- Criar o schema public caso não exista
CREATE SCHEMA IF NOT EXISTS public;

-- Criar sequências (execute esses comandos conectado ao banco myfinance)
CREATE SEQUENCE IF NOT EXISTS public.category_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS public.financial_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS public.role_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS public.transaction_seq START WITH 1 INCREMENT BY 1;

-- Criar tabela de categorias
CREATE TABLE IF NOT EXISTS public.categories (
                                                 id          BIGINT NOT NULL PRIMARY KEY,
                                                 name        VARCHAR(255),
    description VARCHAR(255),
    color       VARCHAR(255),
    enabled     BOOLEAN,
    deleted     BOOLEAN
    );

-- Criar tabela de roles
CREATE TABLE IF NOT EXISTS public.role (
                                           id   BIGINT NOT NULL PRIMARY KEY,
                                           name VARCHAR(255)
    );

-- Criar tabela de usuários
CREATE TABLE IF NOT EXISTS public.user_ (
                                            id       UUID NOT NULL PRIMARY KEY,
                                            name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone    VARCHAR(255),
    active   BOOLEAN
    );

-- Criar tabela de registros financeiros
CREATE TABLE IF NOT EXISTS public.financials (
                                                 id          BIGINT NOT NULL PRIMARY KEY,
                                                 name        VARCHAR(255),
    description VARCHAR(255),
    owner_id    UUID NOT NULL,
    created_at  TIMESTAMP(6),
    CONSTRAINT fko82g9eyhimski0f6esjgl0din FOREIGN KEY (owner_id) REFERENCES public.user_ (id)
    );

-- Criar tabela de compartilhamento financeiro
CREATE TABLE IF NOT EXISTS public.financial_shares (
                                                       financial_id BIGINT NOT NULL,
                                                       user_id      UUID NOT NULL,
                                                       PRIMARY KEY (financial_id, user_id),
    CONSTRAINT fk55ab4ju0kkvs5t4wc1s8qpekg FOREIGN KEY (financial_id) REFERENCES public.financials (id),
    CONSTRAINT fkl6k5eq99788bxa8vaccyj6iqu FOREIGN KEY (user_id) REFERENCES public.user_ (id)
    );

-- Criar tabela de transações
CREATE TABLE IF NOT EXISTS public.transactions (
                                                   id             BIGINT NOT NULL PRIMARY KEY,
                                                   transaction_id BIGINT NOT NULL,
                                                   description    VARCHAR(255),
    amount         NUMERIC(38, 2),
    date           DATE,
    due_date       DATE,
    type           VARCHAR(255) CHECK (type IN ('INCOME', 'EXPENSE')),
    payment_method VARCHAR(255) CHECK (payment_method IN ('CREDIT_CARD', 'DEBIT_CARD', 'PIX', 'BANK_TRANSFER', 'CASH', 'DIGITAL_WALLET')),
    income_source  VARCHAR(255),
    category_id    BIGINT,
    recurring      BOOLEAN,
    CONSTRAINT fkt3i83nkbjedf1xtd35lb9k7mx FOREIGN KEY (transaction_id) REFERENCES public.financials (id),
    CONSTRAINT fksqqi7sneo04kast0o138h19mv FOREIGN KEY (category_id) REFERENCES public.categories (id)
    );

-- Criar tabela de relacionamento entre usuários e roles
CREATE TABLE IF NOT EXISTS public.user_roles (
                                                 user_id UUID NOT NULL,
                                                 role_id BIGINT NOT NULL,
                                                 PRIMARY KEY (role_id, user_id),
    CONSTRAINT fkrhfovtciq1l558cw6udg0h0d3 FOREIGN KEY (role_id) REFERENCES public.role (id),
    CONSTRAINT fki11ca3b26vh3bcucnq39inil3 FOREIGN KEY (user_id) REFERENCES public.user_ (id)
    );

-- Inserir roles básicas
INSERT INTO public.role (id, name)
SELECT nextval('role_seq'), 'ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM public.role WHERE name = 'ADMIN');

INSERT INTO public.role (id, name)
SELECT nextval('role_seq'), 'USER'
    WHERE NOT EXISTS (SELECT 1 FROM public.role WHERE name = 'USER');

-- Limpar categorias existentes (opcional - remova ou comente esta linha se quiser manter categorias existentes)
-- DELETE FROM public.categories;

-- Inserir categorias de DESPESAS
INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Alimentação', 'Gastos com alimentação e supermercado', '#FF5733', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Alimentação');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Transporte', 'Gastos com transporte e locomoção', '#33FF57', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Transporte');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Moradia', 'Gastos com aluguel, condomínio e manutenção', '#3357FF', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Moradia');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Lazer', 'Gastos com entretenimento e diversão', '#F3FF33', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Lazer');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Educação', 'Gastos com cursos, livros e material escolar', '#FF33E9', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Educação');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Saúde', 'Gastos com plano de saúde, medicamentos e consultas', '#33FFF6', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Saúde');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Vestuário', 'Gastos com roupas e acessórios', '#D633FF', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Vestuário');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Utilidades', 'Gastos com água, luz, gás e internet', '#FF8C33', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Utilidades');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Restaurantes', 'Gastos com alimentação fora de casa', '#FF3333', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Restaurantes');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Impostos', 'Gastos com impostos e taxas', '#334DFF', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Impostos');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Viagens', 'Gastos com viagens e turismo', '#33FFB8', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Viagens');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Presente', 'Gastos com presentes para outras pessoas', '#FF33A8', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Presente');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Tecnologia', 'Gastos com eletrônicos e serviços digitais', '#3393FF', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Tecnologia');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Pets', 'Gastos com animais de estimação', '#B833FF', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Pets');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Beleza', 'Gastos com produtos de beleza e cuidados pessoais', '#FF33D1', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Beleza');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Assinaturas', 'Gastos com serviços de assinatura', '#33FFC7', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Assinaturas');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Seguro', 'Gastos com seguros diversos', '#337BFF', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Seguro');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Outros Gastos', 'Despesas não classificadas em outras categorias', '#A6A6A6', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Outros Gastos');

-- Inserir categorias de RECEITAS (~7 categorias)
INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Salário', 'Receita com salário e remuneração fixa', '#4CAF50', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Salário');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Investimentos', 'Receita com rendimentos de investimentos', '#FFC107', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Investimentos');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Freelance', 'Receita com trabalhos freelance e autônomos', '#8BC34A', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Freelance');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Aluguel', 'Receita com aluguel de imóveis', '#03A9F4', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Aluguel');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Presente Recebido', 'Receita com presentes e bonificações', '#9C27B0', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Presente Recebido');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Reembolso', 'Receita com reembolsos e estornos', '#E91E63', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Reembolso');

INSERT INTO public.categories (id, name, description, color, enabled, deleted)
SELECT nextval('category_seq'), 'Outras Receitas', 'Receitas não classificadas em outras categorias', '#4CAF50', true, false
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE name = 'Outras Receitas');