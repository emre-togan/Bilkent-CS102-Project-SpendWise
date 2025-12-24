package com.spendwise.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class Icons {

    public static Icon getDashboardIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {

                Path2D path = new Path2D.Double();
                path.moveTo(width / 2.0, 2);
                path.lineTo(2, height * 0.45); 
                path.lineTo(2, height - 2); 
                path.lineTo(width - 2, height - 2); 
                path.lineTo(width - 2, height * 0.45);
                path.closePath();

                path.moveTo(width * 0.4, height - 2);
                path.lineTo(width * 0.4, height * 0.6);
                path.lineTo(width * 0.6, height * 0.6);
                path.lineTo(width * 0.6, height - 2);

                g2.setStroke(new BasicStroke(2f));
                g2.draw(path);
            }
        };
    }

    public static Icon getBudgetIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {

                RoundRectangle2D rect = new RoundRectangle2D.Double(2, 4, width - 4, height - 8, 4, 4);
                g2.setStroke(new BasicStroke(2f));
                g2.draw(rect);

                g2.fill(new Rectangle2D.Double(2, 8, width - 4, 4));

                g2.draw(new Rectangle2D.Double(6, 15, 6, 4));
            }
        };
    }

    public static Icon getExpensesIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {
  
                Path2D path = new Path2D.Double();
                path.moveTo(4, 2);
                path.lineTo(width - 4, 2);
                path.lineTo(width - 4, height - 2);
                path.lineTo(width * 0.75, height - 5);
                path.lineTo(width * 0.5, height - 2);
                path.lineTo(width * 0.25, height - 5);
                path.lineTo(4, height - 2);
                path.closePath();

                g2.setStroke(new BasicStroke(2f));
                g2.draw(path);

                g2.draw(new Line2D.Double(8, 8, width - 8, 8));
                g2.draw(new Line2D.Double(8, 12, width - 8, 12));
                g2.draw(new Line2D.Double(8, 16, width * 0.6, 16));
            }
        };
    }

    public static Icon getShopIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {
                // Shopping Bag
                int bagW = width - 8;
                int bagH = height - 8;
                int x = 4;
                int y = 8;

                g2.setStroke(new BasicStroke(2f));
                g2.draw(new RoundRectangle2D.Double(x, y, bagW, bagH, 4, 4));

                // Handle
                g2.draw(new Arc2D.Double(width / 2.0 - 5, 2, 10, 10, 0, 180, Arc2D.OPEN));

                // Circle detail
                g2.fill(new Ellipse2D.Double(width / 2.0 - 2, y + bagH / 2.0 - 2, 4, 4));
            }
        };
    }

    public static Icon getChatIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {
                // Chat Bubble
                path = new Path2D.Double();
                path.moveTo(2, height * 0.6);
                path.curveTo(2, 2, width - 2, 2, width - 2, height * 0.6);
                path.curveTo(width - 2, height - 4, width * 0.7, height - 4, width * 0.6, height - 4);
                path.lineTo(width * 0.3, height); // tail
                path.lineTo(width * 0.4, height - 4);
                path.curveTo(width * 0.4, height - 4, 2, height - 4, 2, height * 0.6);
                path.closePath();

                g2.setStroke(new BasicStroke(2f));
                g2.draw(path);
            }

            Path2D path;
        };
    }

    public static Icon getProfileIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {
                // Person
                g2.setStroke(new BasicStroke(2f));

                // Head
                g2.draw(new Ellipse2D.Double(width / 2.0 - 4, 2, 8, 8));

                // Body
                g2.draw(new Arc2D.Double(4, height - 12, width - 8, 12, 0, 180, Arc2D.OPEN));
            }
        };
    }

    public static Icon getSettingsIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {
                // Simple Gear (Circle with dots)
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new Ellipse2D.Double(4, 4, width - 8, height - 8));
                g2.draw(new Ellipse2D.Double(width / 2.0 - 2, height / 2.0 - 2, 4, 4));

                Stroke dashed = new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f,
                        new float[] { 4f }, 0f);
                g2.setStroke(dashed);
                g2.draw(new Ellipse2D.Double(2, 2, width - 4, height - 4));
            }
        };
    }

    public static Icon getLogoutIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {
                g2.setStroke(new BasicStroke(2f));

                // Circle
                g2.draw(new Arc2D.Double(2, 2, width - 4, height - 4, 45, 270, Arc2D.OPEN));

                // Line 
                g2.draw(new Line2D.Double(width / 2.0, height / 2.0, width / 2.0, 2));
            }
        };
    }

    public static Icon getSearchIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {
                g2.setStroke(new BasicStroke(2f));
                // Glass
                int r = (int) (width * 0.65);
                g2.draw(new Ellipse2D.Double(2, 2, r, r));
                // Handle
                g2.draw(new Line2D.Double(r - 2, r - 2, width - 2, height - 2));
            }
        };
    }

    public static Icon getAddIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new Line2D.Double(width / 2.0, 2, width / 2.0, height - 2));
                g2.draw(new Line2D.Double(2, height / 2.0, width - 2, height / 2.0));
            }
        };
    }

    public static Icon getEditIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {
                // Pencil
                Path2D path = new Path2D.Double();
                path.moveTo(width - 6, 2);
                path.lineTo(width - 2, 6);
                path.lineTo(width * 0.4, height - 2);
                path.lineTo(2, height - 2);
                path.lineTo(2, height * 0.6);
                path.lineTo(width - 6, 2);
                path.closePath();

                g2.setStroke(new BasicStroke(2f));
                g2.draw(path);
            }
        };
    }

    public static Icon getInfoIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new Ellipse2D.Double(2, 2, width - 4, height - 4));
                g2.fill(new Ellipse2D.Double(width / 2.0 - 1.5, 6, 3, 3));
                g2.draw(new Line2D.Double(width / 2.0, 12, width / 2.0, height - 6));
            }
        };
    }

    // Category Icons
    public static Icon getCategoryIcon(String category, int size, Color color) {
        if (category == null)
            return getExpensesIcon(size, color); // Default
        switch (category) {
            case "Food":
                return getFoodIcon(size, color);
            case "Transport":
                return getTransportIcon(size, color);
            case "Shopping":
                return getShopIcon(size, color);
            case "Health":
                return getHealthIcon(size, color);
            case "Entertainment":
                return getEntertainmentIcon(size, color);
            default:
                return getExpensesIcon(size, color);
        }
    }

    public static Icon getFoodIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {
                // Burger shape
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new Arc2D.Double(2, 4, width - 4, height / 2.0, 0, 180, Arc2D.OPEN)); // Top bun
                g2.draw(new RoundRectangle2D.Double(2, height / 2.0 + 2, width - 4, height / 2.0 - 4, 4, 4)); // Bottom
                                                                                                              // bun
                g2.draw(new Line2D.Double(2, height / 2.0, width - 2, height / 2.0)); // Lettuce
            }
        };
    }

    public static Icon getTransportIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {
                // Car front
                g2.setStroke(new BasicStroke(2f));
                Path2D path = new Path2D.Double();
                path.moveTo(4, height - 4);
                path.lineTo(4, height * 0.6);
                path.lineTo(width * 0.2, height * 0.4);
                path.lineTo(width * 0.8, height * 0.4);
                path.lineTo(width - 4, height * 0.6);
                path.lineTo(width - 4, height - 4);
                path.closePath();
                g2.draw(path);

                g2.draw(new Ellipse2D.Double(6, height - 4, 4, 4)); // Wheel
                g2.draw(new Ellipse2D.Double(width - 10, height - 4, 4, 4)); // Wheel
            }
        };
    }

    public static Icon getHealthIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {
                // Cross
                g2.setStroke(new BasicStroke(4f)); // Thicker
                g2.draw(new Line2D.Double(width / 2.0, 4, width / 2.0, height - 4));
                g2.draw(new Line2D.Double(4, height / 2.0, width - 4, height / 2.0));
            }
        };
    }

    public static Icon getEntertainmentIcon(int size, Color color) {
        return new VectorIcon(size, color) {
            @Override
            protected void paintIcon(Graphics2D g2, int width, int height) {

                g2.setStroke(new BasicStroke(2f));
                g2.fill(new Ellipse2D.Double(4, height - 8, 6, 6));
                g2.draw(new Line2D.Double(10, height - 6, 10, 4));
                g2.draw(new Line2D.Double(10, 4, width - 4, 4));
                g2.draw(new Line2D.Double(width - 4, 4, width - 4, height - 10));
                g2.fill(new Ellipse2D.Double(width - 10, height - 12, 6, 6));
            }
        };
    }

    private static abstract class VectorIcon implements Icon {
        private int size;
        private Color color;

        public VectorIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.translate(x, y);
            paintIcon(g2, size, size);
            g2.dispose();
        }

        protected abstract void paintIcon(Graphics2D g2, int width, int height);

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }
}
