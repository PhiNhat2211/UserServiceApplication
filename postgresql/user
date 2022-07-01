--
-- PostgreSQL database dump
--

-- Dumped from database version 14.4
-- Dumped by pg_dump version 14.4

-- Started on 2022-07-01 08:34:27

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 209 (class 1259 OID 16395)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 210 (class 1259 OID 16396)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    role character varying(20) NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 16401)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    email character varying(50) NOT NULL,
    password character varying(75) NOT NULL,
    username character varying(50) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 16406)
-- Name: users_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.users_roles OWNER TO postgres;

--
-- TOC entry 3324 (class 0 OID 16396)
-- Dependencies: 210
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (id, role) FROM stdin;
1	ROLE_ADMIN
2	ROLE_USER
\.


--
-- TOC entry 3325 (class 0 OID 16401)
-- Dependencies: 211
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, email, password, username) FROM stdin;
19	test2@gmail.com	$2a$10$uAtLUzNmefwUvlbPZSqhze7nYc6QNN0Ral4yICTp2MCN9T94tnXNy	test2
21	test3@gmail.com	$2a$10$O2GzJRNhx3lmBgfwOrYCMOGglF0dfex7YR/pAcyZZugvZJEFg3vqq	test3
22	test4@gmail.com	$2a$10$xelP8osf5VzRjEJwaMK55OLkduqmqxcNK4sb7rmS90LXHWuGVPnxW	test4
23	test5@gmail.com	$2a$10$5EtTPa4xBPnBFnyMILdLF.kQm7d.UqPvnZz3bhb2/QxmALOazJGdS	test5
24	test6@gmail.com	$2a$10$T8kKe08N9RdmmCisqWjiDusDeYiN/tPuTuaeXuK4qAY1kLv17cjAe	test6
25	test7@gmail.com	$2a$10$VMCgC9zVZx2dgNOfZf15c.yyaTYMIG0Ez0OYTcLD74V6p9pHT5pqa	test7
26	test8@gmail.com	$2a$10$iWKKhcu55yL/31P1k2ATXuANfaToio9C4Y4RqJywmKVs8aD2E9RV2	test8
27	nhan@gmail.com	$2a$10$HdPD8d09vvYTIkI5oYazo.zSUVc8PrE0NJreaomLTJnxi7J/l37Ly	nhan
1	nhat@gmail.com	$2a$10$tJEQR7sS3FapBu0/5BuXrecABU7d0lTd621N4NCsVH42slDtsIup6	nhat
2	phat@gmail.com	$2a$10$1Ew9OmQUmVI.TDjlbzi4FOFRndIGEz3fyFa1HiOIjEuX6VL23I4gC	phat
\.


--
-- TOC entry 3326 (class 0 OID 16406)
-- Dependencies: 212
-- Data for Name: users_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users_roles (user_id, role_id) FROM stdin;
1	1
2	2
27	2
19	2
21	2
22	2
23	2
24	2
25	2
26	2
\.


--
-- TOC entry 3332 (class 0 OID 0)
-- Dependencies: 209
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hibernate_sequence', 46, true);


--
-- TOC entry 3175 (class 2606 OID 16412)
-- Name: users email_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT email_unique UNIQUE (email);


--
-- TOC entry 3173 (class 2606 OID 16400)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- TOC entry 3177 (class 2606 OID 16414)
-- Name: users username_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT username_unique UNIQUE (username);


--
-- TOC entry 3179 (class 2606 OID 16405)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3181 (class 2606 OID 16410)
-- Name: users_roles users_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- TOC entry 3183 (class 2606 OID 16420)
-- Name: users_roles fk2o0jvgh89lemvvo17cbqvdxaa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fk2o0jvgh89lemvvo17cbqvdxaa FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3182 (class 2606 OID 16415)
-- Name: users_roles fkj6m8fwv7oqv74fcehir1a9ffy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fkj6m8fwv7oqv74fcehir1a9ffy FOREIGN KEY (role_id) REFERENCES public.roles(id);


-- Completed on 2022-07-01 08:34:27

--
-- PostgreSQL database dump complete
--

